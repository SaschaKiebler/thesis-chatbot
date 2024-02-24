
class UIService {
    constructor() {
        this.conversation = document.getElementById('conversation');
    }

    addMessage(message) {
        this.conversation.appendChild(message.element);
    }

    clearMessages() {
        this.conversation.innerHTML = '';
        this.addMessage(new Message(null, "Hallo! Wie kann ich dir in den Themen Gesundheitsinformatik, Telemedizin und dem deutschen Gesundheitswesen behilflich sein?", "ai"));
    }





}