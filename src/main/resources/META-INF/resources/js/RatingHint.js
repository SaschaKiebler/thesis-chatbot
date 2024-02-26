class RatingHint {
    constructor() {
        this.hint = 'Welche Antwort gefällt dir besser? Klicke auf deine bevorzugte Antwort';
        this.element = document.createElement('div');
        this.element.className = `rating-hint`;
        this.element.innerHTML = `
                                        <p>${this.hint}</p>
                                    `;
    }

    setVisible(visible) {
        this.element.className = visible ? `rating-hint` : `rating-hint hidden`;
    }

    setHint(hint) {
        this.hint = hint;
    }

    getHint() {
        return this.hint;
    }
}