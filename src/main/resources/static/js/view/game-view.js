'use strict';

let GameView = new function() {
	
	const AltCharacters = new Map([
		["A", "Ą"], ["C", "Ć"], ["E", "Ę"],
		["L", "Ł"], ["N", "Ń"], ["O", "Ó"],
		["S", "Ś"], ["X", "Ź"], ["Z", "Ż"]
	]);

	const LetterClasses = new Map([
		["0", "letterNotInWord"],
		["1", "letterMisplaced"],
		["2", "letterCorrect"]
	]);

	let revertMap = function(map) {
		return new Map(Array.from(map, entry => [entry[1], entry[0]]));
	};
	
	const LetterClassesIds = revertMap(LetterClasses);

	this.activeWord = "";
	this.activeRow = 0;
	this.canInputLetters = false;
	
	let altPressed = false;

	this.enterGuess = function(guess) {
		// Message format: array of numbers (0 - not in word, 1 - misplaced, 2 - correct) [if array empty => invalid word]
		let json = [];
		for (let i = 0; i < 6; i++) {
			json.push(Math.floor(Math.random() * 3));
		}
		
		if (json.length > 0) {
			for (let i = 0; i < WORD_LENGTH; i++) {
				let letterCell = getLetterCell("activePlayer", this.activeRow, i);
				let letterClass = LetterClasses.get(json[i].toString());
				letterCell.setAttribute("class", letterClass);
				
				let keyId = "letterKey-" + this.activeWord.charAt(i);
				let key = document.getElementById(keyId);
				let keyClass = key.getAttribute("class");
				let keyClassId = LetterClassesIds.get(keyClass);
				console.log(keyClassId);
				
				if (keyClass === null || parseInt(keyClassId) < parseInt(json[i])) {
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

	this.inputLetter = function(letter) {
		if (this.activeRow < TRIES_COUNT) {
			let guessLen = this.activeWord.length;
			if (letter === "<") {
				let letterCell = getLetterCell("activePlayer", this.activeRow, guessLen - 1);
				letterCell.textContent = "";
				
				this.activeWord = this.activeWord.slice(0, -1);
				// TODO: clear invalid word styling
			} else {
				let letterCell = getLetterCell("activePlayer", this.activeRow, guessLen);
				letterCell.textContent = letter;
				
				this.activeWord += letter;
				if (this.activeWord.length === WORD_LENGTH) {
					this.enterGuess(this.activeWord);
				}
			}
		}
		//console.log(letter); // tmp
	};
	
	this.handleKeyboardKeyDown = function(keyCode) {
		if (keyCode.startsWith("Key")) {
			let letter = keyCode.charAt(3);
			if (letter !== "Q" && letter !== "V") {
				if (altPressed) {
					if (AltCharacters.has(letter))
						letter = AltCharacters.get(letter);
					this.inputLetter(letter);
				} else if (letter !== "X") {
					this.inputLetter(letter);
				}
			}
		} else if (keyCode === "Backspace") {
			this.inputLetter("<");
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
			this.inputLetter(letter);
		} else if (id === "backspaceKey") {
			this.inputLetter("<");
		}
	};

	let getLetterCell = function(player, row, column) {
		let cellId = player + "-" + row.toString() + "-" + column.toString();
		return document.getElementById(cellId);
	};
}
