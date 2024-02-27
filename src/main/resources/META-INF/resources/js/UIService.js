
class UIService {
    constructor() {
        this.conversation = document.getElementById('conversation');
        this.MarkdownParser = new MarkdownParser();
    }

    addMessage(message) {
        this.conversation.appendChild(message.element);
    }

    clearMessages() {
        this.conversation.innerHTML = '';
        this.addMessage(new Message(null, "Hallo! Wie kann ich dir in den Themen Gesundheitsinformatik, Telemedizin und dem deutschen Gesundheitswesen behilflich sein?", "ai"));
    }

    toggleDarkMode() {
        document.body.classList.toggle('dark-mode');
        // change image in darkmode button
        document.getElementById('darkmode').firstChild.src = document.body.classList.contains('dark-mode') ? 'pictures/lightmode.svg' : 'pictures/darkmode.svg';
    }





}