'use strict';

let SoloClient = new function() {

    let m_stompClient = undefined;
    let m_sessionId = undefined;

    this.init = function(onConnectionFunction) {
        let connectionResult = GameClientCommon.connectStomp(this.handleReceiveMessage, onConnectionFunction);
        m_stompClient = connectionResult[0];
        m_sessionId = connectionResult[1];
    };

    this.onConnection = function(stompClient, sessionId) {
        stompClient.subscribe("/queue/direct-" + sessionId, this.handleReceiveMessage);
        stompClient.subscribe("/topic/words", this.handleReceiveMessage);
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
            case 220: receiveRandomWord(msg.d[0]); break;
            case 221: receiveGuessResponse(msg.d[0].matches); break;
            default: console.error("Unknown websockets message type.");
        }
    };

    this.sendRandomWordRequest = function() {
        m_stompClient.send("/app/words/solo/random-word", {}, "");
    };

    this.sendGuess = function(wordId, wordGuess) {
        m_stompClient.send("/app/words/solo/guess", {}, "[\"" + wordId + "\",\"" + wordGuess + "\"]");
    };

    let receiveRandomWord = function(wordId) {
        SoloClient.handleRandomWord(wordId);
    };

    let receiveGuessResponse = function(matchList) {
        SoloClient.handleGuessResponse(matchList);
    };

    let receiveDisallowedWordError = function() {
        SoloClient.handleDisallowedWordError();
    };

    this.handleRandomWord = function() {
        throw new Error("This function must be overridden.");
    };

    this.handleGuessResponse = function() {
        throw new Error("This function must be overridden.");
    };

    this.handleDisallowedWordError = function() {
        throw new Error("This function must be overridden.");
    }
}
