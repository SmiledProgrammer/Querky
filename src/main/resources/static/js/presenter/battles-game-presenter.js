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

document.addEventListener("keydown", (event) => {
	let key = event.code;
	GameView.handleKeyboardKeyDown(key);
});

document.addEventListener("keyup", (event) => {
	let key = event.code;
	GameView.handleKeyboardKeyUp(key);
});

let BattlesGamePresenter = new function() {

	let m_tableNumber;
	let m_gameStartTimeLeft;
	let m_roundsLeft;
	let m_roundTimeLeft;
	let m_gameState;
	let m_players;
	let m_timerId;

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
		BattlesClient.init();
		BattlesClient.handleJoinedTableData = this.handleJoinedTableData;
		BattlesClient.handlePlayerJoinedTable = this.handlePlayerJoinedTable;
		BattlesClient.handlePlayerLeftTable = this.handlePlayerLeftTable;
		BattlesClient.handlePlayerReady = this.handlePlayerReady;
		BattlesClient.handleRoundCountdownStart = this.handleRoundCountdownStart;
		BattlesClient.handleRoundGuessingPhaseStart = this.handleRoundGuessingPhaseStart;
		BattlesClient.handleRoundEnd = this.handleRoundEnd;
		BattlesClient.handlePlayerGuess = this.handlePlayerGuess;
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

	this.handleJoinedTableData = function(tableNumber, gameStartTime, roundsLeft, roundTime, gameState, players) {
		m_tableNumber = tableNumber;
		m_gameStartTimeLeft = gameStartTime;
		m_roundsLeft = roundsLeft;
		m_roundTimeLeft = roundTime;
		m_gameState = parseGameState(gameState);
		m_players = fetchPlayers(players);
		if (m_players.size >= 2) {
			startGameStartCountdown();
		}
	};

	this.handlePlayerJoinedTable = function(nickname) {
		if (BattlesClient.getClientNickname() !== nickname) {
			let player = {
				"isPlaying": true,
				"points": 0,
				"letterMatches": []
			};
			m_players.set(nickname, player);
			if (m_players.size >= 2) {
				startGameStartCountdown();
			}
		}
	};

	this.handlePlayerLeftTable = function(nickname) {
		if (BattlesClient.getClientNickname() === nickname) {
			resetGameState();
		} else {
			m_players.delete(nickname);
			if (m_players.size <= 1) {
				if (m_gameState === GameStates.GameStartCountdown) {
					cancelGameStartCountdown();
				} else if (m_gameState !== GameStates.WaitingForPlayers) {
					endGame();
				}
			}
		}
	};

	this.handlePlayerReady = function(nickname, ready) {
		// TODO
	};

	this.handleRoundCountdownStart = function() {
		if (m_gameState === GameStates.GameStartCountdown) {
			startGame();
		}
		if (m_roundsLeft > 0) {
			m_gameState = GameStates.RoundStartCountdown;
			m_roundTimeLeft = ROUND_START_COUNTDOWN_DURATION + ROUND_DURATION
		} else {
			endGame();
		}
	};

	this.handleRoundGuessingPhaseStart = function() {
		m_gameState = GameStates.GuessingPhase;
	};

	this.handleRoundEnd = function(pointsList) {
		m_gameState = GameStates.RoundEnding;
		m_roundsLeft -= 1;
		for (let nickname in pointsList) {
			m_players.get(nickname).points = pointsList[nickname];
			m_players.get(nickname).letterMatches = [];
		}
	};

	this.handlePlayerGuess = function(nickname, hitList) {
		// TODO
	};
	
	let startGame = function() {
		m_gameState = GameStates.RoundStartCountdown;
		for (let nickname of m_players.keys()) {
			m_players.get(nickname).points = 0;
			m_players.get(nickname).letterMatches = [];
		}
		m_roundsLeft = ROUND_COUNT;
	};

	let startGameStartCountdown = function() {
		m_gameState = GameStates.GameStartCountdown;
		m_gameStartTimeLeft = GAME_START_COUNTDOWN_DURATION;
		startTimer();
	};

	let cancelGameStartCountdown = function() {
		m_gameState = GameStates.WaitingForPlayers;
		stopTimer();
	};

	let handleTimerTick = function() {
		if (m_gameState === GameStates.GameStartCountdown) {
			if (m_gameStartTimeLeft > 0) {
				m_gameStartTimeLeft -= 1;
				console.log("Game timer tick!"); // TODO: remove
			}
		} else if (m_gameState === GameStates.RoundStartCountdown ||
					m_gameState === GameStates.GuessingPhase ||
					m_gameState === GameStates.RoundEnding) {
			if (m_roundTimeLeft > 0) {
				m_roundTimeLeft -= 1;
				console.log("Round timer tick!"); // TODO: remove
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
		for (let nickname of m_players.keys()) {
			m_players.get(nickname).isPlaying = false;
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
				letterMatches.push(matchObj.matches);
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
}
