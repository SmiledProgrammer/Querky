'use strict';

let SoloGamePresenter = new function () {

    let m_wordId;

    this.init = function () {
        SoloClient.init(this.onConnection);
        SoloClient.handleRandomWord = this.handleRandomWord;
        SoloClient.handleGuessResponse = this.handleGuessResponse;
        SoloClient.handleDisallowedWordError = this.handleDisallowedWordError;
        SoloView.initForSoloGame(SoloGamePresenter);
    };

    this.onConnection = function () {
        SoloClient.sendRandomWordRequest();
    };

    this.makeGuess = function (guess) {
        console.log("Sending guess: (" + m_wordId + ", " + guess + ")");
        SoloClient.sendGuess(m_wordId, guess);
    };

    this.handleRandomWord = function (wordId) {
        m_wordId = wordId;
    };

    this.handleGuessResponse = function (matchList) {
        SoloView.markGuess(matchList);
    };

    this.handleDisallowedWordError = function () {
        SoloView.markGuess(null);
    }
}
