
/*
    Diese Klasse stellt eine Nachricht dar und formatiert sie für die UI. Je nach Sender wird die Nachricht mit Bot oder You beschriftet
 */

class Message {

    constructor(id, message, sender) {
        this.message = message;
        this.markdownParser = new MarkdownParser();
        this.message = this.markdownParser.parse(this.message);
        this.id = id;
        this.sender = sender;
        this.element = document.createElement('div');
        this.element.className = `${this.sender}-message message`;
        if(this.id){
            this.element.id = this.id;
        }
        this.element.innerHTML = `<div class="icon"></div>
                                    <div class="message-content">
                                        <div class="name">${this.sender === "ai" ? "Bot" : "You"}</div>    
                                        <p class="text">${this.message}</p>
                                    </div>`;
    }

    setMessage(message) {
        this.message = message;
        this.element.querySelector('.text').innerHTML = this.message;
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