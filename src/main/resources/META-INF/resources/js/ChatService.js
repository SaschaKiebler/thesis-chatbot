class ChatService {

    constructor() {
        this.messages = [];
        this.conversationId = null;
        this.uiService = new UIService();
    }

    addMessage(message) {
        this.messages.push(message);
    }

    async getAnswer(requestText) {
        this.addMessage(new Message(null, requestText, "user"));
        this.addMessagesToUI();

        try {
            const response = await fetch(
                `/llm/commercial?${this.conversationId ? `conversationId=${this.conversationId}&` : ''}message=${requestText}`,
                {
                    method: 'POST'

                });

            const text = await response.text(); // Get the response as text
            console.log('Raw response:', text);

            try {
                const data = JSON.parse(text); // Try parsing as JSON
                this.addMessage(new Message(data.answerId, data.answer, "ai"));
                this.conversationId = data.conversationId;
                this.addMessagesToUI();
            } catch (parseError) {
                console.error('Error parsing JSON:', parseError);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }


    getMessages() {
        return this.messages;
    }

    addMessagesToUI() {
        this.messages.forEach(message => {
            this.uiService.addMessage(message);
        });
    }

    clearMessages() {
        this.messages = [];
        this.conversationId = null;
        this.uiService.clearMessages();
    }
}