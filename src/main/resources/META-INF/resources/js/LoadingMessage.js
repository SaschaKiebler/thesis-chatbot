class LoadingMessage extends Message{
    constructor() {
        super("loading-message", "", "ai");
        const loadingDots = document.createElement('div');
        loadingDots.className = 'dot-bricks';
        this.setMessage(loadingDots.outerHTML);

    }
}