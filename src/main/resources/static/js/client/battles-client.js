'use strict';

let BattlesClient = new function() {

	let stompClient = undefined;

	this.init = function() {
		let socket = new SockJS("/querky");
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			let sessionId = /\/([^\/]+)\/websocket/.exec(socket._transport.url)[1];
			console.log("Connected to Querky. Session ID is: " + sessionId);
			stompClient.subscribe("/queue/direct-" + sessionId, BattlesClient.handleReceiveMessage);
			stompClient.subscribe("/topic/words", BattlesClient.handleReceiveMessage)
			stompClient.send("/app/words/join-table", {}, "[\"100\"]");
		});
	};

	this.handleReceiveMessage = function(rawMsg) {
		let msg = convertRawMessage(rawMsg);
		switch (msg.c) {
			case 101: receiveJoinedTableData(msg.d[0], msg.d[1], msg.d[2], msg.d[3], msg.d[4]); break;
			case 102: receivePlayerJoinedTable(msg.d[0]); break;
			case 103: receivePlayerLeftTable(msg.d[0]); break;
			case 105: receivePlayerReady(msg.d[0]); break;
			case 111: receiveGameStart(); break;
			case 221: receivePlayerGuess(msg.d[0], msg.d[1]); break;
			case 222: receiveRoundEnd(msg.d[0]); break;
			default: console.error("Unknown websockets message type.");
		}
	};

	let convertRawMessage = function(rawMsg) {
		try {
			let noEscapesMsg = rawMsg.body.replace(/\\"/g, '"');
			return JSON.parse(noEscapesMsg);
		} catch (err) {
			console.error("Couldn't parse websockets message JSON content.");
		}
		return null;
	};
	
	/* Endpoint: /words/join-table */
	this.sendJoinTable = function(tableNumber) {
		// TODO: set BattlesClient.handleReceiveTableMessage as /topic/table websocket handler
	};
	
	/* Endpoint: /words/leave-table */
	this.sendLeaveTable = function() {
		
	};
	
	/* Endpoint: /words/ready */
	this.sendReadinessDeclaration = function(ready) {
		
	};
	
	/* Endpoint: /words/guess */
	this.sendGuess = function(wordGuess) {
		
	};

	let receiveJoinedTableData = function(tableNumberStr, gameTimeStr, roundTimeStr, gameStateStr, playersStr) {
		let tableNumber = parseInt(tableNumberStr);
		let gameTime = parseInt(gameTimeStr);
		let roundTime = parseInt(roundTimeStr);
		// let gameState = fetchGameStateFromString(gameStateStr);
		// let players = fetchPlayersFromString(playersStr);
		let gameState = gameStateStr; // TODO: remove
		let players = playersStr; // TODO: remove
		BattlesClient.handleJoinedTableData(tableNumber, gameTime, roundTime, gameState, players);
	};

	let receivePlayerJoinedTable = function(nickname) {

	};

	let receivePlayerLeftTable = function(nickname) {

	};

	let receivePlayerReady = function(nickname, ready) {

	};

	let receiveGameStart = function() {

	};

	let receivePlayerGuess = function(nickname, hitList) {

	};
	
	let receiveRoundEnd = function(pointsList) {

	};

	this.handleJoinedTableData = function(tableNumber, gameTime, roundTime, gameState, players) {
		throw new Error("This function must be overridden.");
	};

	this.handlePlayerJoinedTable = function(nickname) {
		throw new Error("This function must be overridden.");
	};

	this.handlePlayerLeftTable = function(nickname) {
		throw new Error("This function must be overridden.");
	};

	this.handlePlayerReady = function(nickname, ready) {
		throw new Error("This function must be overridden.");
	};

	this.handleGameStart = function() {
		throw new Error("This function must be overridden.");
	};

	this.handlePlayerGuess = function(nickname, hitList) {
		throw new Error("This function must be overridden.");
	};

	this.handleRoundEnd = function(pointsList) {
		throw new Error("This function must be overridden.");
	};
}
