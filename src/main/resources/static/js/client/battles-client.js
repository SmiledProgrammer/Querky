'use strict';

let BattlesClient = new function() {
	
	this.init = function() {
		// TODO: set BattlesClient.handleReceiveTableMessage as /topic/table websocket handler
	};
	
	/* Endpoint: /topic/table/NUMBER */
	this.handleReceiveTableMessage = function(rawMsg) {
		let msg = convertRawMessage(rawMsg);
		switch (msg.c) {
			case 101: this.receiveJoinedTableData(msg.d[0], msg.d[1], msg.d[2], msg.d[3], msg.d[4]); break;
			case 102: this.receivePlayerJoinedTable(msg.d[0]); break;
			case 103: this.receivePlayerLeftTable(msg.d[0]); break;
			case 105: this.receivePlayerReady(msg.d[0]); break;
			case 111: this.receiveGameStart(); break;
			case 221: this.receivePlayerGuess(msg.d[0], msg.d[1]); break;
			case 222: this.receiveRoundEnd(msg.d[0]); break;
			default: console.log("[ERROR] Unknown websockets message type.");
		}
	};

	let convertRawMessage = function(rawMsg) {
		try {
			return JSON.parse(rawMsg);
		} catch (err) {
			console.log("[ERROR] Couldn't parse websockets message JSON content.");
		}
		return null;
	};
	
	/* Endpoint: /words/join-table */
	this.sendJoinTable = function(tableNumber) {
		
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
	
	this.receiveJoinedTableData = function(tableNumber, gameTime, roundTime, gameState, players) {
		throw new Error("This function must be overridden.");
	};
	
	this.receivePlayerJoinedTable = function(nickname) {
		throw new Error("This function must be overridden.");
	};
	
	this.receivePlayerLeftTable = function(nickname) {
		throw new Error("This function must be overridden.");
	};
	
	this.receivePlayerReady = function(nickname, ready) {
		throw new Error("This function must be overridden.");
	};
	
	this.receiveGameStart = function() {
		throw new Error("This function must be overridden.");
	};
	
	this.receivePlayerGuess = function(nickname, hitList) {
		throw new Error("This function must be overridden.");
	};
	
	this.receiveRoundEnd = function(pointsList) {
		throw new Error("This function must be overridden.");
	};
}
