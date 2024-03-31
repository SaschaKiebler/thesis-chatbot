
/*
    class to represent two messages side by side in the UI
 */
class DualMessage {
    constructor(messageLeft, messageRight) {
        this.id = messageLeft.id+messageRight.id;
        this.messageLeft = messageLeft;
        this.messageRight = messageRight;
        this.element = document.createElement('div');
        this.element.className = `duo-message`;
        this.element.id = this.id;
        this.element.innerHTML = `${this.messageLeft.element.outerHTML}
                                    ${this.messageRight.element.outerHTML}
                                    `;

    }



}