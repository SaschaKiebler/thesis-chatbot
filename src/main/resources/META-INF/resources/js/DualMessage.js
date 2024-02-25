class DualMessage {
    constructor(messageLeft, messageRight) {
        this.messageLeft = messageLeft;
        this.messageRight = messageRight;
        this.element = document.createElement('div');
        this.element.className = `duo-message`;
        this.element.innerHTML = `${this.messageLeft.element.outerHTML}
                                    ${this.messageRight.element.outerHTML}
                                    `;

    }



}