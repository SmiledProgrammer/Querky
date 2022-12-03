'use strict';

let SoloView = new function() {

	const AltCharacters = new Map([
		["A", "Ą"], ["C", "Ć"], ["E", "Ę"],
		["L", "Ł"], ["N", "Ń"], ["O", "Ó"],
		["S", "Ś"], ["X", "Ź"], ["Z", "Ż"]
	]);

	this.canInputLetters = false;

	let altPressed = false;
	let requestHandler = undefined;

	this.init = function(presenter) {
		requestHandler = presenter;
		this.setup();
	};

	this.setup = function() {
		this.activeWord = "";
		this.activeRow = 0;
		for (let row = 0; row < TRIES_COUNT; row++) {
			for (let col = 0; col < WORD_LENGTH; col++) {
				let letterCell = GameViewCommon.getPlayersLetterCell("activePlayer", row, col);
				letterCell.textContent = "";
				letterCell.removeAttribute("class");
			}
		}
		let keyboardRows = document.getElementsByClassName("keyboardRow");
		for (let keysRow of keyboardRows) {
			for (let key of keysRow.children) {
				key.removeAttribute("class");
			}
		}
	};

	this.enterGuess = function(guessWord) {
		requestHandler.makeGuess(guessWord);
	};

	this.markGuess = function(matchList) {
		if (matchList !== null) {
			for (let i = 0; i < WORD_LENGTH; i++) {
				let letterCell = GameViewCommon.getPlayersLetterCell("activePlayer", this.activeRow, i);
				let letterClass = GameViewCommon.LetterClasses.get(matchList[i].toString());
				letterCell.setAttribute("class", letterClass);
				
				let keyId = "letterKey-" + this.activeWord.charAt(i);
				let key = document.getElementById(keyId);
				let keyClass = key.getAttribute("class");
				let keyClassId = GameViewCommon.LetterClassesIds.get(keyClass);

				if (keyClass === null || parseInt(keyClassId) < parseInt(matchList[i])) {
					key.setAttribute("class", letterClass);
				}
			}
			this.activeWord = "";
			this.activeRow++;
		} else {
			let rowId = "activePlayer-" + this.activeRow.toString();
			let invalidRow = document.getElementById(rowId);
			invalidRow.setAttribute("class", "invalidWord");
		}
	};

	this.inputCharacter = function(letter) {
		if (this.canInputLetters === true && this.activeRow < TRIES_COUNT) {
			if (letter === "<") {
				this.handleBackspaceInput();
			} else {
				this.handleLetterInput(letter);
			}
		}
	};

	this.handleBackspaceInput = function() {
		if (this.activeWord.length === WORD_LENGTH) {
			let wordRow = document.getElementById("activePlayer-" + this.activeRow);
			wordRow.removeAttribute("class");
		}
		if (this.activeWord.length > 0) {
			let letterCell = GameViewCommon.getPlayersLetterCell(
				"activePlayer", this.activeRow, this.activeWord.length - 1);
			letterCell.textContent = "";
			this.activeWord = this.activeWord.slice(0, -1);
		}
	};

	this.handleLetterInput = function(letter) {
		if (this.activeWord.length < WORD_LENGTH) {
			let letterCell = GameViewCommon.getPlayersLetterCell(
				"activePlayer", this.activeRow, this.activeWord.length);
			letterCell.textContent = letter;
			this.activeWord += letter;
			if (this.activeWord.length === WORD_LENGTH) {
				this.enterGuess(this.activeWord);
			}
		}
	};
	
	this.handleKeyboardKeyDown = function(keyCode) {
		if (keyCode.startsWith("Key")) {
			let letter = keyCode.charAt(3);
			if (letter !== "Q" && letter !== "V") {
				if (altPressed) {
					if (AltCharacters.has(letter))
						letter = AltCharacters.get(letter);
					this.inputCharacter(letter);
				} else if (letter !== "X") {
					this.inputCharacter(letter);
				}
			}
		} else if (keyCode === "Backspace") {
			this.inputCharacter("<");
		} else if (keyCode === "AltRight") {
			altPressed = true;
		}
	};
	
	this.handleKeyboardKeyUp = function(keyCode) {
		if (keyCode === "AltRight") {
			altPressed = false;
		}
	};

	this.handleKeyboardPress = function(id) {
		if (id.startsWith("letterKey-")) {
			let letter = id.charAt(10);
			this.inputCharacter(letter);
		} else if (id === "backspaceKey") {
			this.inputCharacter("<");
		}
	};

	let evaluateWord = function(guessWord) {
		// TODO: copy this logic from backend or transfer responsibility to backend
	};
}
