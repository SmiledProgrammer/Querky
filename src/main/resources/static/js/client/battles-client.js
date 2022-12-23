'use strict';

let BattlesClient = new function() {

	let m_stompClient;
	let m_tableSubscriptionId;

	this.init = function(onConnectionFunction) {
		m_stompClient = GameClientCommon.connectStomp(this.handleReceiveMessage, onConnectionFunction);
	};

	this.handleReceiveMessage = function(rawMsg) {
		let msg = GameClientCommon.convertRawJson(rawMsg);
		if (msg.error === true) {
			if (msg.c === 922) {
				receiveDisallowedWordError();
			} else {
				console.error("Received error message: " + msg.d[0]);
			}
			return;
		}
		switch (msg.c) {
			case 101: receiveJoinedTableData(msg.d[0], msg.d[1], msg.d[2], msg.d[3], msg.d[4], msg.d[5], msg.d[6]); break;
			case 102: receivePlayerJoinedTable(msg.d[0]); break;
			case 103: receivePlayerLeftTable(msg.d[0]); break;
			case 105: receivePlayerReady(msg.d[0]); break;
			case 211: receiveRoundCountdownStart(); break;
			case 212: receiveRoundGuessingPhaseStart(); break;
			case 213: receiveRoundEnd(msg.d[0]); break;
			case 221: receivePlayerGuess(msg.d[0], msg.d[1].matches); break;
			default: console.error("Unknown websockets message type.");
		}
	};

	this.sendJoinTable = function(tableNumber) {
		m_stompClient.send("/app/words/join-table", {}, "[" + tableNumber + "]");
	};

	this.sendLeaveTable = function() {
		m_stompClient.send("/app/words/leave-table", {}, "[]");
	};

	this.sendReadinessDeclaration = function(ready) {
		m_stompClient.send("/app/words/ready", {}, "[" + ready + "]");
	};

	this.sendGuess = function(wordGuess) {
		m_stompClient.send("/app/words/guess", {}, "[\"" + wordGuess + "\"]");
	};

	let receiveJoinedTableData = function(tableNumber, gameStartTime, roundsLeft, roundTime,
										  gameState, players, joiningPlayerIndex) {
		BattlesClient.handleJoinedTableData(tableNumber, gameStartTime, roundsLeft, roundTime,
											gameState, players, joiningPlayerIndex);
		let tableSubscription = m_stompClient.subscribe("/topic/table-" + tableNumber, BattlesClient.handleReceiveMessage);
		m_tableSubscriptionId = tableSubscription.id;
	};

	let receivePlayerJoinedTable = function(username) {
		BattlesClient.handlePlayerJoinedTable(username);
	};

	let receivePlayerLeftTable = function(username) {
		if (BattlesClient.getClientUsername() === username) {
			m_stompClient.unsubscribe(m_tableSubscriptionId);
			m_tableSubscriptionId = undefined;
		}
		BattlesClient.handlePlayerLeftTable(username);
	};

	let receivePlayerReady = function(username, ready) {
		BattlesClient.handlePlayerReady(username, ready);
	};

	let receiveRoundCountdownStart = function() {
		BattlesClient.handleRoundCountdownStart();
	};

	let receiveRoundGuessingPhaseStart = function() {
		BattlesClient.handleRoundGuessingPhaseStart();
	};

	let receiveRoundEnd = function(pointsList) {
		BattlesClient.handleRoundEnd(pointsList);
	};

	let receivePlayerGuess = function(username, matchList) {
		BattlesClient.handlePlayerGuess(username, matchList);
	};

	let receiveDisallowedWordError = function() {
		BattlesClient.handleDisallowedWordError();
	};

	this.handleJoinedTableData = function() {
		throw new Error("This function must be overridden.");
	};

	this.handlePlayerJoinedTable = function() {
		throw new Error("This function must be overridden.");
	};

	this.handlePlayerLeftTable = function() {
		throw new Error("This function must be overridden.");
	};

	this.handlePlayerReady = function() {
		throw new Error("This function must be overridden.");
	};

	this.handleRoundCountdownStart = function() {
		throw new Error("This function must be overridden.");
	};

	this.handleRoundGuessingPhaseStart = function() {
		throw new Error("This function must be overridden.");
	};

	this.handleRoundEnd = function() {
		throw new Error("This function must be overridden.");
	};

	this.handlePlayerGuess = function() {
		throw new Error("This function must be overridden.");
	};

	this.handleDisallowedWordError = function() {
		throw new Error("This function must be overridden.");
	}
}
