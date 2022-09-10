'use strict';

const GAME_START_COUNTDOWN_DURATION = 15;
const GAME_DURATION = 15 * 60;
const ROUND_START_COUNTDOWN_DURATION = 10;
const ROUND_DURATION = 60;
const POINTS_TO_WIN = 200;

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

	let tableNumber = 0;
	let gameTimeLeft = GAME_DURATION + GAME_START_COUNTDOWN_DURATION;
	let roundTimeLeft = ROUND_DURATION + ROUND_START_COUNTDOWN_DURATION;
	let gameState = undefined;
	let playersOnTable = [];

	let initMemberVariables = function() {
		tableNumber = 0;
		gameTimeLeft = GAME_DURATION + GAME_START_COUNTDOWN_DURATION;
		roundTimeLeft = ROUND_DURATION + ROUND_START_COUNTDOWN_DURATION;
		gameState = undefined;
		playersOnTable = [];
	};

	this.init = function() {
		initMemberVariables();
		BattlesClient.init();
		BattlesClient.receiveJoinedTableData = this.handleJoinedTableData;
		BattlesClient.receivePlayerJoinedTable = this.handlePlayerJoinedTable;
		BattlesClient.receivePlayerLeftTable = this.handlePlayerLeftTable;
		BattlesClient.receivePlayerReady = this.handlePlayerReady;
		BattlesClient.receiveGameStart = this.handleGameStart;
		BattlesClient.receivePlayerGuess = this.handlePlayerGuess;
		BattlesClient.receiveRoundEnd = this.handleRoundEnd;
	};

	this.joinTable = function(tableNumber) {
		BattlesClient.sendJoinTable(tableNumber);
	};
	
	this.leaveTable = function() {
		BattlesClient.sendLeaveTable();
		initMemberVariables();
		//stopGameTimer();
		//stopRoundTimer();
	};

	this.declareReadiness = function(ready) {
		BattlesClient.sendReadinessDeclaration(ready);
	};
	
	this.makeGuess = function(guess) {
		BattlesClient.sendGuess(guess);
	};
	
	this.handleJoinedTableData = function(tableNumber, gameTime, roundTime, gameState, players) {
		this.tableNumber = tableNumber;
		this.gameTimeLeft = gameTime;
		this.roundTimeLeft = roundTime;
		//this.gameState = somehowConvertGameState(gameState); // TODO
		this.playerOnTable = players;
		
		//startGameTimer()
		//startRoundTimer()
	};
	
	this.handlePlayerJoinedTable = function(nickname) {
		
	};
	
	this.handlePlayerLeftTable = function(nickname) {
		
	};

	this.handlePlayerReady = function(nickname, ready) {

	};

	this.handleGameStart = function() {
		
	};
	
	this.handlePlayerGuess = function(nickname, hitList) {
		
	};
	
	this.handleRoundEnd = function(pointsList) {
		
	};
}
