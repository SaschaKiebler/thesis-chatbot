/*
* Klasse um Antwortgenerierung dynamischer zu machen
* */
class LoadingMessage extends Message{
    constructor(message) {
        super("loading-message", message, "ai");
        const loadingDots = document.createElement('div');
        loadingDots.className = 'dot-bricks';
        this.setMessage(loadingDots.outerHTML);

    }
}