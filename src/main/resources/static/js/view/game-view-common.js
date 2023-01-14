'use strict';

let GameViewCommon = new function () {

    this.LetterClasses = new Map([
        ["0", "letterNotInWord"],
        ["1", "letterMisplaced"],
        ["2", "letterCorrect"]
    ]);

    let revertMap = function (map) {
        return new Map(Array.from(map, entry => [entry[1], entry[0]]));
    };

    this.LetterClassesIds = revertMap(this.LetterClasses);

    document.addEventListener("keydown", (event) => {
        SoloView.handleKeyboardKeyDown(event.code);
    });

    document.addEventListener("keyup", (event) => {
        SoloView.handleKeyboardKeyUp(event.code);
    });

    this.getPlayersLetterCell = function (player, row, column) {
        let cellId = player + "-" + row.toString() + "-" + column.toString();
        return document.getElementById(cellId);
    };
}
