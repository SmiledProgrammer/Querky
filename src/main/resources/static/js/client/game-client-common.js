'use strict';

let GameClientCommon = new function () {

    this.connectStomp = function (msgHandlingFunction, onConnectionFunction) {
        let socket = new SockJS("/querky");
        let stompClient = Stomp.over(socket);
        stompClient.connect({}, function () {
            let sessionId = /\/([^\/]+)\/websocket/.exec(socket._transport.url)[1];
            console.log("Connected to Querky. Session ID is: " + sessionId);
            stompClient.subscribe("/queue/direct-" + sessionId, msgHandlingFunction);
            stompClient.subscribe("/topic/words", msgHandlingFunction);
            if (onConnectionFunction !== undefined) {
                onConnectionFunction(stompClient, sessionId);
            }
        });
        return stompClient;
    };

    this.convertRawJson = function (rawJson) {
        try {
            let noEscapesMsg = rawJson.body.replace(/\\"/g, '"');
            return JSON.parse(noEscapesMsg);
        } catch (err) {
            console.error("Couldn't parse websockets message JSON content.");
        }
        return null;
    };

}
