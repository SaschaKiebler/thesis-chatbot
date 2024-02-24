class Message {

    constructor(id, message, sender) {
        this.message = message;
        this.id = id;
        this.sender = sender;
        this.element = document.createElement('div');
        this.element.className = `${this.sender}-message message`;
        this.element.id = this.id;
        this.element.innerHTML = `<div class="icon"></div>
                                    <div class="message-content">
                                        <div class="name">${this.sender === "ai" ? "Bot" : "You"}</div>    
                                        <p>${this.message}</p>
                                    </div>`;
    }

    getMessage() {
        return this.message;
    }

    getSender() {
        return this.sender;
    }

    getId() {
        return this.id;
    }

}