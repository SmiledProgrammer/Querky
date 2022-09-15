'use strict';

let BattlesView = new function() {

    const nicknames = ["Niszczyciel", "Stokrotka", "Władziu", "XDkox"]; // TODO: remove

    let m_tableNumberSpan;
    let m_roundNumberSpan;
    let m_roundTimerSpan;
    let m_playerDivIds;

    this.init = function() {
        m_tableNumberSpan = document.getElementById("tableNumber");
        m_roundNumberSpan = document.getElementById("roundNumber");
        m_roundTimerSpan = document.getElementById("roundTimer");
        m_playerDivIds = new Map();
    };

    this.updateViewOnJoinTableData = function(activePlayerUsername, tableNumber, gameStartTime,
                                              roundNumber, roundTime, players) {
        setTableNumber(tableNumber);
        // TODO: handle game time left
        setRoundNumber(roundNumber);
        setRoundTimeLeft(roundTime);
        m_playerDivIds.set(activePlayerUsername, 0);
        let playerUsernames = Array.from(players.keys());
        let activePlayerIndex = playerUsernames.indexOf(activePlayerUsername);
        for (let i = 0; i < players.size; i++) {
            let username;
            if (i !== activePlayerIndex) {
                username = playerUsernames[i];
                m_playerDivIds.set(username, i);
            } else {
                username = activePlayerUsername;
            }
            getPlayerUsernameSpan(username).textContent = nicknames[i]; // TODO: remove
            let score = players.get(username).points;
            getPlayerScoreSpan(username).textContent = "- " + score + " -";
        }
    };

    this.updateViewOnPlayerJoinedTable = function(username) {
        let playerDivId = m_playerDivIds.size;
        m_playerDivIds.set(username, playerDivId);
        getPlayerUsernameSpan(username).textContent = nicknames[playerDivId]; // TODO: remove
        getPlayerScoreSpan(username).textContent = "- 0 -";
    };

    this.updateViewOnPlayerLeftTable = function(username) {
        // TODO
    };

    this.updateViewOnGameStartCountdownStart = function() {

    };

    this.updateViewOnGameStartCountdownCancel = function() {

    };

    this.updateViewOnGameStart = function() {

    };

    this.updateViewOnBreakGame = function() {

    };

    this.updateViewOnRoundCountdownStart = function() {

    };

    this.updateViewOnGuessingPhaseStart = function() {

    };

    this.updateViewOnRoundEnd = function() {

    };

    this.updateViewOnPlayerGuess = function() {

    };

    this.updateViewOnGameEnd = function() {

    };

    let setTableNumber = function(tableNumber) {
        m_tableNumberSpan.textContent = "Stół #" + tableNumber;
    };

    let setRoundNumber = function(roundNumber) {
        m_roundNumberSpan.textContent = "Runda " + roundNumber;
    };

    let setRoundTimeLeft = function(roundTime) {
        let minutes = Math.floor(roundTime / 60);
        let seconds = roundTime % 60;
        m_roundTimerSpan.textContent = minutes + ":" + seconds;
    };

    let getPlayerUsernameSpan = function(username) {
        let playerId = m_playerDivIds.get(username);
        let usernameSpanId = (playerId === 0) ? "activeUsername" : "opponentUsername-" + playerId;
        return document.getElementById(usernameSpanId);
    };

    let getPlayerScoreSpan = function(username) {
        let playerId = m_playerDivIds.get(username);
        let scoreSpanId = (playerId === 0) ? "activeScore" : "opponentScore-" + playerId;
        return document.getElementById(scoreSpanId);
    };

    let getPlayerBoardCell = function(username, row, column) {

    };

    let getPlayerOverlayDiv = function(username) {

    };

    let getMainOverlayDiv = function() {
        let divId = "activePlayer-overlay";
        return document.getElementById(divId);
    };
}
