'use strict';

document.addEventListener("keydown", (event) => {
	let key = event.code;
	GameView.handleKeyboardKeyDown(key);
});

document.addEventListener("keyup", (event) => {
	let key = event.code;
	GameView.handleKeyboardKeyUp(key);
});

let SoloGamePresenter = new function() {
	
}
