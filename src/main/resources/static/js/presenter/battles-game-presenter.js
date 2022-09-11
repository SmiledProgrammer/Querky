'use strict';

const GAME_START_COUNTDOWN_DURATION = 15;
const ROUND_COUNT = 20;
const ROUND_START_COUNTDOWN_DURATION = 10;
const ROUND_DURATION = 60;

const GameStates = {
	WaitingForPlayers: Symbol("waitingForPlayers"),
	GameStarting: Symbol("gameStarting"),
	RoundStarting: Symbol("roundStarting"),
	Guessing: Symbol("guessing"),
	RoundEnding: Symbol("roundEnding"),
	GameEnding: Symbol("gameEnding")
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
	let m_playersOnTable;

	let resetGameState = function() {
		m_tableNumber = 0;
		m_gameStartTimeLeft = GAME_START_COUNTDOWN_DURATION;
		m_roundsLeft = ROUND_COUNT;
		m_roundTimeLeft = ROUND_DURATION + ROUND_START_COUNTDOWN_DURATION;
		m_gameState = undefined;
		m_playersOnTable = [];
	};

	this.init = function() {
		resetGameState();
		BattlesClient.init();
		BattlesClient.handleJoinedTableData = this.handleJoinedTableData;
		BattlesClient.handlePlayerJoinedTable = this.handlePlayerJoinedTable;
		BattlesClient.handlePlayerLeftTable = this.handlePlayerLeftTable;
		BattlesClient.handlePlayerReady = this.handlePlayerReady;
		BattlesClient.handleGameStart = this.handleGameStart;
		BattlesClient.handlePlayerGuess = this.handlePlayerGuess;
		BattlesClient.handleRoundEnd = this.handleRoundEnd;
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
		m_playersOnTable = fetchPlayers(players);
		// BattlesView.joinTable(m_tableNumber, m_gameStartTimeLeft, m_roundsLeft,
		//                       m_roundTimeLeft, m_gameState, m_playersOnTable);
	};

	this.handlePlayerJoinedTable = function(nickname) {
		if (BattlesClient.getClientNickname() !== nickname) {
			//BattlesView.addPlayerToGame(nickname);
		}
	};

	this.handlePlayerLeftTable = function(nickname) {
		if (BattlesClient.getClientNickname() === nickname) {
			//BattlesView.leaveTable();
			resetGameState();
		} else {
			//BattlesView.removePlayerFromGame(nickname);
		}
	};

	this.handlePlayerReady = function(nickname, ready) {

	};

	this.handleGameStart = function() {

	};

	this.handlePlayerGuess = function(nickname, hitList) {

	};

	this.handleRoundEnd = function(pointsList) {

	};

	let parseGameState = function(gameStateStr) {
		switch (gameStateStr) {
			case "WAITING_FOR_PLAYERS": return GameStates.WaitingForPlayers;
			case "GAME_STARTING": return GameStates.GameStarting;
			case "ROUND_STARTING": return GameStates.RoundStarting;
			case "GUESSING": return GameStates.Guessing;
			case "ROUND_ENDING": return GameStates.RoundEnding;
			case "GAME_ENDING": return GameStates.GameEnding;
			default: return undefined;
		}
	};

	let fetchPlayers = function(players) {
		let fetchedPlayers = [];
		for (let player of players) {
			let fetchedPlayer = {
				"nickname": player.username,
				"isPlaying": player.isPlaying,
				"points": player.points
			};
			fetchedPlayers.push(fetchedPlayer);
		}
		return fetchedPlayers;
	};
}
