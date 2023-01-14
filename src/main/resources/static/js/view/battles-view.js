'use strict';

let BattlesView = new function () {

    let m_tableNumberSpan;
    let m_roundNumberSpan;
    let m_roundTimerSpan;
    let m_playerDivIds;

    this.init = function () {
        m_tableNumberSpan = document.getElementById("tableNumber");
        m_roundNumberSpan = document.getElementById("roundNumber");
        m_roundTimerSpan = document.getElementById("roundTimer");
        m_playerDivIds = new Map();
    };

    this.updateViewOnJoinTableData = function (activePlayerUsername, tableNumber, gameStartTime,
                                               roundNumber, roundTime, players) {
        setTableNumber(tableNumber);
        // TODO: handle game time left
        setRoundNumber(roundNumber);
        this.setRoundTimeLeft(roundTime);
        m_playerDivIds.set(activePlayerUsername, 0);
        let playerUsernames = Array.from(players.keys());
        let activePlayerIndex = playerUsernames.indexOf(activePlayerUsername);
        for (let i = 0; i < players.size; i++) {
            let username = playerUsernames[i];
            if (i < activePlayerIndex) {
                m_playerDivIds.set(username, i + 1);
            } else if (i > activePlayerIndex) {
                m_playerDivIds.set(username, i);
            }
            getPlayerUsernameSpan(username).textContent = username;
            let score = players.get(username).points;
            getPlayerScoreSpan(username).textContent = "- " + score + " -";
        }
    };

    this.updateViewOnPlayerJoinedTable = function (username) {
        let playerDivId = m_playerDivIds.size;
        m_playerDivIds.set(username, playerDivId);
        getPlayerUsernameSpan(username).textContent = username;
        getPlayerScoreSpan(username).textContent = "- 0 -";
    };

    this.updateViewOnPlayerLeftTable = function (username) {
        // TODO
    };

    this.updateViewOnGameStartCountdownStart = function () {
        setAllTableOverlaysGray();
    };

    this.updateViewOnRoundCountdownStart = function () {
        setAllTableWithoutOverlaysGray();
    };

    this.updateViewOnGuessingPhaseStart = function () {
        clearAllTableOverlays();
    };

    this.updateViewOnRoundEnd = function (pointsList) {
        for (const [username, _] of m_playerDivIds) {
            let points = pointsList[username];
            getPlayerScoreSpan(username).textContent = "- " + points + " -";
        }
    };

    this.updateViewOnOpponentGuess = function (username, matchList, activeRow) {
        if (matchList.length > 0) {
            let opponentId = m_playerDivIds.get(username);
            for (let i = 0; i < WORD_LENGTH; i++) {
                let letterCell = GameViewCommon.getPlayersLetterCell("opponent-" + opponentId, activeRow, i);
                let letterClass = GameViewCommon.LetterClasses.get(matchList[i].toString());
                letterCell.setAttribute("class", letterClass);
            }
        }
    };

    this.updateViewOnGameEnd = function () {
        // TODO
    };

    // TODO: make this function used only in context of currently running round (not when round countdown is going)
    this.setRoundTimeLeft = function (roundTime) {
        let minutes = Math.floor(roundTime / 60);
        let seconds = (roundTime % 60).toLocaleString(
            'en-US', {minimumIntegerDigits: 2, useGrouping: false});
        m_roundTimerSpan.textContent = minutes + ":" + seconds;
        if (roundTime > ROUND_DURATION) {
            setRoundStartCountdownTimeLeft(roundTime - ROUND_DURATION);
        }
    };

    this.setGameStartCountdownTimeLeft = function (time) {
        let mainOverlayDiv = getMainOverlayDiv();
        mainOverlayDiv.textContent = "Gra zaczyna się za " + time + " sekund...";
    };

    this.markPlayerHasGuessed = function (username) {
        let overlayDiv = getPlayerOverlayDiv(username);
        overlayDiv.setAttribute("class", "overlay overlayGreen");
    };

    this.markPlayerHasFailedToGuess = function (username) {
        let overlayDiv = getPlayerOverlayDiv(username);
        overlayDiv.setAttribute("class", "overlay overlayRed");
    };

    this.resetOpponentsTables = function () {
        for (let i = 1; i < PLAYERS_COUNT; i++) {
            for (let row = 0; row < TRIES_COUNT; row++) {
                for (let col = 0; col < WORD_LENGTH; col++) {
                    let letterCell = GameViewCommon.getPlayersLetterCell("opponent-" + i, row, col);
                    letterCell.removeAttribute("class");
                }
            }
        }
    };

    let setRoundStartCountdownTimeLeft = function (time) {
        let mainOverlayDiv = getMainOverlayDiv();
        mainOverlayDiv.textContent = "Runda zaczyna się za " + time + " sekund...";
    };

    let setAllTableOverlaysGray = function () {
        let overlayDivs = document.getElementsByClassName("overlay");
        for (let overlay of overlayDivs) {
            overlay.setAttribute("class", "overlay overlayGray");
        }
    };

    let setAllTableWithoutOverlaysGray = function () {
        let overlayDivs = document.getElementsByClassName("overlay");
        for (let overlay of overlayDivs) {
            if (overlay.getAttribute("class") === "overlay") {
                overlay.setAttribute("class", "overlay overlayGray");
            }
        }
    };

    let clearAllTableOverlays = function () {
        let overlayDivs = document.getElementsByClassName("overlay");
        for (let overlay of overlayDivs) {
            overlay.setAttribute("class", "overlay");
            overlay.textContent = "";
        }
    };

    let setTableNumber = function (tableNumber) {
        m_tableNumberSpan.textContent = "Stół #" + tableNumber;
    };

    let setRoundNumber = function (roundNumber) {
        m_roundNumberSpan.textContent = "Runda " + roundNumber;
    };

    let getPlayerUsernameSpan = function (username) {
        let playerId = m_playerDivIds.get(username);
        let usernameSpanId = (playerId === 0) ? "activeUsername" : "opponentUsername-" + playerId;
        return document.getElementById(usernameSpanId);
    };

    let getPlayerScoreSpan = function (username) {
        let playerId = m_playerDivIds.get(username);
        let scoreSpanId = (playerId === 0) ? "activeScore" : "opponentScore-" + playerId;
        return document.getElementById(scoreSpanId);
    };

    let getPlayerOverlayDiv = function (username) {
        let playerId = m_playerDivIds.get(username);
        let divPrefix = (playerId === 0) ? "activePlayer-" : "opponent-" + playerId;
        let divId = divPrefix + "-overlay";
        return document.getElementById(divId);
    };

    let getMainOverlayDiv = function () {
        let divId = "activePlayer-overlay";
        return document.getElementById(divId);
    };
}
