'use strict';

const MenuOptions = [
    ["/home.html", "Start"],
    ["/words/solo.html", "Solo"],
    ["/words/battles.html", "Bitwy"]
];

let WebsiteUI = new function () {

    this.renderHeader = function () {
        let nav = document.createElement("nav");
        nav.setAttribute("class", "navbar");

        let gameTitle = document.createElement("a");
        gameTitle.setAttribute("href", "/home");
        gameTitle.setAttribute("class", "gameTitle");
        gameTitle.textContent = "Word game";
        nav.appendChild(gameTitle);

        let separator = document.createElement("div");
        separator.setAttribute("class", "separator");
        nav.appendChild(separator);

        let navbarMenu = this.createNavbarMenu();
        nav.appendChild(navbarMenu);

        let header = document.createElement("header");
        header.appendChild(nav);
        let body = document.body;
        body.prepend(header);
    };

    this.createNavbarMenu = function () {
        let navbarMenu = document.createElement("div");
        navbarMenu.setAttribute("class", "navbarMenu");
        for (const menuOption of MenuOptions) {
            let optionLink = document.createElement("a");
            optionLink.setAttribute("href", menuOption[0]);
            optionLink.textContent = menuOption[1];
            navbarMenu.appendChild(optionLink);
        }
        return navbarMenu;
    };

    this.renderFooter = function () {
        // TODO
    };
}
