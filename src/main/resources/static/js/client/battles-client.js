'use strict';

let BattlesClient = new function() {

	let m_stompClient;
	let m_sessionId;
	let m_tableSubscriptionId;

	this.init = function() {
		let socket = new SockJS("/querky");
		m_stompClient = Stomp.over(socket);
		m_stompClient.connect({}, function() {
			m_sessionId = /\/([^\/]+)\/websocket/.exec(socket._transport.url)[1];
			console.log("Connected to Querky. Session ID is: " + m_sessionId);
			m_stompClient.subscribe("/queue/direct-" + m_sessionId, BattlesClient.handleReceiveMessage);
			m_stompClient.subscribe("/topic/words", BattlesClient.handleReceiveMessage);

			BattlesClient.sendJoinTable(100); // TODO: execute differently
		});
	};

	this.handleReceiveMessage = function(rawMsg) {
		let msg = BattlesClient.convertRawJson(rawMsg);
		if (msg.error === true) {
			console.error("Received error message: " + msg.d[0]);
			return;
		}
		switch (msg.c) {
			case 101: receiveJoinedTableData(msg.d[0], msg.d[1], msg.d[2], msg.d[3], msg.d[4], msg.d[5]); break;
			case 102: receivePlayerJoinedTable(msg.d[0]); break;
			case 103: receivePlayerLeftTable(msg.d[0]); break;
			case 105: receivePlayerReady(msg.d[0]); break;
			case 211: receiveRoundCountdownStart(); break;
			case 212: receiveRoundGuessingPhaseStart(); break;
			case 213: receiveRoundEnd(msg.d[0]); break;
			case 221: receivePlayerGuess(msg.d[0], msg.d[1]); break;
			default: console.error("Unknown websockets message type.");
		}
	};

	this.convertRawJson = function(rawJson) {
		try {
			let noEscapesMsg = rawJson.body.replace(/\\"/g, '"');
			return JSON.parse(noEscapesMsg);
		} catch (err) {
			console.error("Couldn't parse websockets message JSON content.");
		}
		return null;
	};

	this.getClientNickname = function() {
		return m_sessionId;
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
		m_stompClient.send("/app/words/guess", {}, "[" + wordGuess + "]");
	};

	let receiveJoinedTableData = function(tableNumber, gameStartTime, roundsLeft, roundTime, gameState, players) {
		BattlesClient.handleJoinedTableData(tableNumber, gameStartTime, roundsLeft, roundTime, gameState, players);
		let tableSubscription = m_stompClient.subscribe("/topic/table-" + tableNumber, BattlesClient.handleReceiveMessage);
		m_tableSubscriptionId = tableSubscription.id;
	};

	let receivePlayerJoinedTable = function(nickname) {
		BattlesClient.handlePlayerJoinedTable(nickname);
	};

	let receivePlayerLeftTable = function(nickname) {
		if (BattlesClient.getClientNickname() === nickname) {
			m_stompClient.unsubscribe(m_tableSubscriptionId);
			m_tableSubscriptionId = undefined;
		}
		BattlesClient.handlePlayerLeftTable(nickname);
	};

	let receivePlayerReady = function(nickname, ready) {
		BattlesClient.handlePlayerReady(nickname, ready);
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

	let receivePlayerGuess = function(nickname, hitList) {
		BattlesClient.handlePlayerGuess(nickname, hitList);
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
}
