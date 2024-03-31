/*
* Class to display a loading message in the UI with three dots that circulate as a loading animation
* */
class LoadingMessage extends Message{
    constructor(message) {
        super("loading-message", message, "ai");
        const loadingDots = document.createElement('div');
        loadingDots.className = 'dot-bricks';
        this.setMessage(loadingDots.outerHTML);

    }
}