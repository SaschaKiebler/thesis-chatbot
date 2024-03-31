/*
* Class to display a loading message in the UI with three dots that circulate as a loading animation
* */
class LoadingMessage extends Message{
    constructor(message) {
        super("loading-message", null, "ai");
        const loadingDots = document.createElement('div');
        loadingDots.className = 'dot-bricks';
        const messageElement = document.createElement('div');
        messageElement.innerHTML = message;
        const container = document.createElement('div');
        container.className = 'loading-with-message';
        container.appendChild(loadingDots);
        container.appendChild(messageElement);

        this.setMessage(container.outerHTML);

    }
}