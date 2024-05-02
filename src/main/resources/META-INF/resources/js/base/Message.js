
/*
    This class represents a single message. It has a sender, a message and an id. if the sender is 'ai', the message is a bot message
 */

class Message {

    constructor(id, message, sender, modelname) {
        this.message = message;
        this.id = id;
        this.sender = sender;
        this.modelname = modelname;
        this.element = document.createElement('div');
        this.element.className = `${this.sender}-message message`;
        if(this.id){
            this.element.id = this.id;
        }
        this.element.innerHTML = `<div class="icon"></div>
                                    <div class="message-content">
                                        <div class="name">${this.sender === "ai" ? "Bot" : "You"}</div>
                                        <div class="modelname">${this.modelname !== null && this.modelname !== undefined ? this.modelname : ""}</div>    
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