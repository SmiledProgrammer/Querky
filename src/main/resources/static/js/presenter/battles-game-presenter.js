'use strict';

const GAME_START_COUNTDOWN_DURATION = 15;
const ROUND_COUNT = 5;
const ROUND_START_COUNTDOWN_DURATION = 10;
const ROUND_DURATION = 90;

const GameStates = {
	WaitingForPlayers: Symbol("waitingForPlayers"),
	GameStartCountdown: Symbol("gameStartCountdown"),
	RoundStartCountdown: Symbol("roundStartCountdown"),
	GuessingPhase: Symbol("guessingPhase"),
	RoundEnding: Symbol("roundEnding")
};

let BattlesGamePresenter = new function() {

	let m_tableNumber;
	let m_gameStartTimeLeft;
	let m_roundsLeft;
	let m_roundTimeLeft;
	let m_gameState;
	let m_players;
	let m_timerId;
	let m_activePlayerUsername;

	let resetGameState = function() {
		m_tableNumber = 0;
		m_gameStartTimeLeft = GAME_START_COUNTDOWN_DURATION;
		m_roundsLeft = ROUND_COUNT;
		m_roundTimeLeft = ROUND_DURATION + ROUND_START_COUNTDOWN_DURATION;
		m_gameState = GameStates.WaitingForPlayers;
		m_players = new Map();
		m_timerId = undefined;
		stopTimer();
	};

	this.init = function() {
		resetGameState();
		BattlesClient.init(this.onConnection);
		BattlesClient.handleJoinedTableData = this.handleJoinedTableData;
		BattlesClient.handlePlayerJoinedTable = this.handlePlayerJoinedTable;
		BattlesClient.handlePlayerLeftTable = this.handlePlayerLeftTable;
		BattlesClient.handlePlayerReady = this.handlePlayerReady;
		BattlesClient.handleRoundCountdownStart = this.handleRoundCountdownStart;
		BattlesClient.handleRoundGuessingPhaseStart = this.handleRoundGuessingPhaseStart;
		BattlesClient.handleRoundEnd = this.handleRoundEnd;
		BattlesClient.handlePlayerGuess = this.handlePlayerGuess;
		BattlesClient.handleDisallowedWordError = this.handleDisallowedWordError;
		SoloView.initForBattlesGame(BattlesGamePresenter);
		BattlesView.init();
	};

	this.onConnection = function() {
		BattlesClient.sendJoinTable(100); // TODO: execute differently
	};


	this.joinTable = function(tableNumber) {
		BattlesClient.sendJoinTable(tableNumber);
	};

	this.leaveTable = function() {
		BattlesClient.sendLeaveTable();
	};

	this.declareReadiness = function(ready) {
		BattlesClient.sendReadinessDeclaration(ready);
	};

	this.makeGuess = function(guess) {
		BattlesClient.sendGuess(guess);
	};

	this.handleJoinedTableData = function(tableNumber, gameStartTime, roundsLeft, roundTime,
										  gameState, players, joiningPlayerIndex) {
		m_activePlayerUsername = players[joiningPlayerIndex].username;
		m_tableNumber = tableNumber;
		m_gameStartTimeLeft = gameStartTime;
		m_roundsLeft = roundsLeft;
		m_roundTimeLeft = roundTime;
		m_gameState = parseGameState(gameState);
		m_players = fetchPlayers(players);
		if (m_players.size >= 2) {
			startGameStartCountdown();
		}
		// TODO: load actual game state in any moment of the game
		let roundNumber = ROUND_COUNT - m_roundsLeft + 1;
		BattlesView.updateViewOnJoinTableData(m_activePlayerUsername, m_tableNumber, m_gameStartTimeLeft,
											  roundNumber, m_roundTimeLeft, m_players);
	};

	this.handlePlayerJoinedTable = function(username) {
		if (!isActivePlayer(username)) {
			let player = {
				"isPlaying": true,
				"points": 0,
				"letterMatches": []
			};
			m_players.set(username, player);
			if (m_players.size >= 2) {
				startGameStartCountdown();
			}
			BattlesView.updateViewOnPlayerJoinedTable(username);
		}
	};

	this.handlePlayerLeftTable = function(username) {
		if (isActivePlayer(username)) {
			resetGameState();
		} else {
			m_players.delete(username);
			if (m_players.size <= 1) {
				if (m_gameState === GameStates.GameStartCountdown) {
					cancelGameStartCountdown();
				} else if (m_gameState !== GameStates.WaitingForPlayers) {
					endGame();
				}
			}
		}
		BattlesView.updateViewOnPlayerLeftTable(username);
	};

	this.handlePlayerReady = function(username, ready) {
		// TODO
	};

	this.handleRoundCountdownStart = function() {
		if (m_gameState === GameStates.GameStartCountdown) {
			startGame();
		}
		if (m_roundsLeft > 0) {
			m_gameState = GameStates.RoundStartCountdown;
			m_roundTimeLeft = ROUND_START_COUNTDOWN_DURATION + ROUND_DURATION + 1;
			BattlesView.setRoundTimeLeft(m_roundTimeLeft);
			BattlesView.updateViewOnRoundCountdownStart();
		} else {
			endGame();
		}
	};

	this.handleRoundGuessingPhaseStart = function() {
		m_gameState = GameStates.GuessingPhase;
		SoloView.canInputLetters = true;
		BattlesView.updateViewOnGuessingPhaseStart();
		setupRound();
	};

	this.handleRoundEnd = function(pointsList) {
		m_gameState = GameStates.RoundEnding;
		SoloView.canInputLetters = false;
		m_roundsLeft -= 1;
		for (let username in pointsList) {
			m_players.get(username).points = pointsList[username];
			m_players.get(username).letterMatches = [];
		}
		BattlesView.updateViewOnRoundEnd(pointsList);
	};

	this.handlePlayerGuess = function(username, matchList) {
		m_players.get(username).letterMatches.push(matchList);
		if (isActivePlayer(username)) { 
			SoloView.markGuess(matchList);
		} else {
			let opponentsActiveRow = m_players.get(username).letterMatches.length - 1;
			BattlesView.updateViewOnOpponentGuess(username, matchList, opponentsActiveRow);
			if (isCorrectMatch(matchList)) {
				BattlesView.markPlayerHasGuessed(username);
			} else if (m_players.get(username).letterMatches.length === TRIES_COUNT) {
				BattlesView.markPlayerHasFailedToGuess(username);
			}
		}
	};

	this.handleDisallowedWordError = function() {
		SoloView.markGuess(null);
	};

	let startGame = function() {
		m_gameState = GameStates.RoundStartCountdown;
		for (let username of m_players.keys()) {
			m_players.get(username).points = 0;
			m_players.get(username).letterMatches = [];
		}
		m_roundsLeft = ROUND_COUNT;
	};

	let startGameStartCountdown = function() {
		m_gameState = GameStates.GameStartCountdown;
		m_gameStartTimeLeft = GAME_START_COUNTDOWN_DURATION;
		startTimer();
		BattlesView.updateViewOnGameStartCountdownStart();
	};

	let cancelGameStartCountdown = function() {
		m_gameState = GameStates.WaitingForPlayers;
		stopTimer();
	};

	let setupRound = function() {
		for (const [key, value] of m_players) {
			m_players.get(key).letterMatches = [];
		}
		BattlesView.resetOpponentsTables();
		SoloView.setup();
	};

	let handleTimerTick = function() {
		if (m_gameState === GameStates.GameStartCountdown) {
			if (m_gameStartTimeLeft > 0) {
				m_gameStartTimeLeft -= 1;
				BattlesView.setGameStartCountdownTimeLeft(m_gameStartTimeLeft);
			}
		} else if (m_gameState === GameStates.RoundStartCountdown ||
					m_gameState === GameStates.GuessingPhase ||
					m_gameState === GameStates.RoundEnding) {
			if (m_roundTimeLeft > 0) {
				m_roundTimeLeft -= 1;
				BattlesView.setRoundTimeLeft(m_roundTimeLeft);
			}
		}
	};

	let startTimer = function() {
		stopTimer();
		m_timerId = setInterval(handleTimerTick, 1000);
	};

	let stopTimer = function() {
		if (m_timerId !== undefined) {
			clearInterval(m_timerId);
			m_timerId = undefined;
		}
	};

	let endGame = function() {
		stopTimer();
		m_gameState = GameStates.WaitingForPlayers;
		for (let username of m_players.keys()) {
			m_players.get(username).isPlaying = false;
		}
	};

	let parseGameState = function(gameStateStr) {
		switch (gameStateStr) {
			case "WAITING_FOR_PLAYERS": return GameStates.WaitingForPlayers;
			case "GAME_START_COUNTDOWN": return GameStates.GameStartCountdown;
			case "ROUND_START_COUNTDOWN": return GameStates.RoundStartCountdown;
			case "GUESSING_PHASE": return GameStates.GuessingPhase;
			case "ROUND_ENDING": return GameStates.RoundEnding;
			default: return undefined;
		}
	};

	let fetchPlayers = function(players) {
		let fetchedPlayers = new Map();
		for (let player of players) {
			let letterMatches = [];
			for (let matchObj of player.letterMatches) {
				letterMatches.push(matchObj.matches); // TODO: fix (probably wrong)
			}
			let fetchedPlayer = {
				"isPlaying": player.isPlaying,
				"points": player.points,
				"letterMatches": letterMatches
			};
			fetchedPlayers.set(player.username, fetchedPlayer);
		}
		return fetchedPlayers;
	};
	
	let isActivePlayer = function(username) {
		return username === m_activePlayerUsername;
	};

	let isCorrectMatch = function(matchList) {
		for (let i = 0; i < matchList.length; i++) {
			if (matchList[i] !== 2) {
				return false;
			}
		}
		return true;
	};
}
