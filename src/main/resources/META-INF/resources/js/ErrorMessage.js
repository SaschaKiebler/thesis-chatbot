class ErrorMessage extends Message{
    constructor(id, message, sender) {
        super(id, message, sender);
        this.element.classList.add('error-message');
    }

}