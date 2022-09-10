'use strict';

const WORD_LENGTH = 6;
const TRIES_COUNT = 6;

const PLAYERS_COUNT = 4; // TODO: make dynamic

// TODO: move .table/.td styles to separate ones (not children of other classes)
// TODO: make elements invisible until fully rendered
// TODO: select current subpage on navbar menu

let GameUI = new function() {
	
	this.renderKeyboard = function() {
		const keyboardLayout = [ "WERTYUIOP", "ASDFGHJKL", "ZCBNM<", "ĄĆĘŁŃÓŚŹŻ" ];
		let keyboardDiv = document.getElementById("keyboard");
		for (const rowKeys of keyboardLayout) {
			let keyboardRow = document.createElement("div");
			keyboardRow.setAttribute("class", "keyboardRow");
			for (let i = 0; i < rowKeys.length; i++) {
				let keyButton = document.createElement("button");
				let letter = rowKeys.charAt(i);
				let buttonId = (letter !== "<") ? ("letterKey-" + letter) : ("backspaceKey");
				keyButton.setAttribute("id", buttonId);
				keyButton.setAttribute("onClick", "GameView.handleKeyboardPress(this.id)");
				keyButton.textContent = letter;
				keyboardRow.appendChild(keyButton);
			}
			keyboardDiv.appendChild(keyboardRow);
		}
	};
	
	this.renderActivePlayerInfo = function() {
		let activeBoard = document.getElementsByClassName("activeBoard");
		for (let boardDiv of activeBoard) {
			createUsernameAndScore(boardDiv, "activeUsername", "activeScore", false);
		}
	};

	this.createOpponentBoards = function() {
		let opponentsColumn = document.getElementsByClassName("opponentBoards");
		for (let col of opponentsColumn) {
			for (let i = 0; i < PLAYERS_COUNT - 1; i++) {
				let idStr = i.toString();
				
				let boardDiv = document.createElement("div");
				let boardId = "opponent-" + idStr;
				boardDiv.setAttribute("id", boardId);
				boardDiv.setAttribute("class", "opponentBoard playerBoard");
				
				let usernameId = "opponentUsername-" + idStr;
				let scoreId = "opponentScore-" + idStr;
				createUsernameAndScore(boardDiv, usernameId, scoreId, true);
				
				col.appendChild(boardDiv);
			}
		}
	};
	
	let createUsernameAndScore = function(baseDiv, usernameSpanId, scoreSpanId, isOpponent) {
		let usernameSpan = document.createElement("div");
		let scoreSpan = document.createElement("div");
		usernameSpan.setAttribute("id", usernameSpanId);
		scoreSpan.setAttribute("id", scoreSpanId);
		if (isOpponent === false) {
			usernameSpan.setAttribute("class", "playerUsername activeUsername");
			scoreSpan.setAttribute("class", "playerScore activeScore");
		} else {
			usernameSpan.setAttribute("class", "playerUsername opponentUsername");
			scoreSpan.setAttribute("class", "playerScore opponentScore");
		}
		usernameSpan.textContent = "paprykarzkielecki@onet.pl"; // tmp
		scoreSpan.textContent = "- 450 -"; // tmp
		baseDiv.appendChild(usernameSpan);
		baseDiv.appendChild(scoreSpan);
	};

	this.renderWordTables = function() {
		let playerBoards = document.getElementsByClassName("playerBoard");
		for (let board of playerBoards) {
			let boardId = board.id;
			let wordTable = document.createElement("table");
			let wordTableBody = document.createElement("tbody");
			wordTable.appendChild(wordTableBody);
			for (let row = 0; row < TRIES_COUNT; row++) {
				let tableRow = document.createElement("tr");
				let rowId = boardId + "-" + row.toString();
				tableRow.setAttribute("id", rowId);
				for (let cell = 0; cell < WORD_LENGTH; cell++) {
					let tableCell = document.createElement("td");
					let cellId = boardId + "-" + row.toString() + "-" + cell.toString();
					tableCell.setAttribute("id", cellId);
					tableRow.appendChild(tableCell);
				}
				wordTableBody.appendChild(tableRow);
			}
			board.appendChild(wordTable);
		}
	};
}
