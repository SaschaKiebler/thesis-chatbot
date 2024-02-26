class ChatService {

    constructor() {
        this.messages = [];
        this.conversationIdleft = null;
        this.conversationIdright = null;
        this.uiService = new UIService();
    }

    addMessage(message) {
        this.messages.push(message);
        this.addMessagesToUI()
    }

    async getAnswer(requestText) {
        this.addMessage(new Message(null, requestText, "user"));

        try {
            const response = await fetch(
                `/llm/leftService?${this.conversationIdleft ? `conversationId=${this.conversationIdleft}&` : ''}message=${requestText}`,
                {
                    method: 'POST'

                });

            const text = await response.text(); // Get the response as text
            console.log('Raw response:', text);

            try {
                const data = JSON.parse(text); // Try parsing as JSON
                if (data.answer){
                    this.addMessage(new Message(data.answerId, data.answer, "ai"));
                }
                else{
                    this.addMessage(new Message(null, "Sorry, das verstehe ich nicht!", "ai"));
                }
                this.conversationIdleft = data.conversationId;
            } catch (parseError) {
                console.error('Error parsing JSON:', parseError);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }

    async getDualAnswer(requestText, ratingHint) {
        this.addMessage(new Message(null, requestText, "user"));

        try {
            const url1 = `/llm/leftService?${this.conversationIdleft ? `conversationId=${this.conversationIdleft}&` : ''}message=${requestText}`;
            const url2 = `/llm/rightService?${this.conversationIdright ? `conversationId=${this.conversationIdright}&` : ''}message=${requestText}`;

            const fetch1 = fetch(url1, { method: 'POST' });
            const fetch2 = fetch(url2, { method: 'POST' });

            const [response1, response2] = await Promise.all([fetch1, fetch2]);

            const text1 = await response1.text();
            const text2 = await response2.text();
            console.log('Raw response1:', text1);
            console.log('Raw response2:', text2);

            try {
                const data1 = JSON.parse(text1);
                const data2 = JSON.parse(text2);

                if (data1.answer && data2.answer) {
                    const messageLeft = new Message(data1.answerId, data1.answer, "ai");
                    const messageRight = new Message(data2.answerId, data2.answer, "ai");
                    this.addMessage(new DualMessage(messageLeft, messageRight));
                    this.addMessage(ratingHint);
                    ratingHint.setVisible(true);
                } else {
                    this.addMessage(new ErrorMessage(null, "Sorry, es gab einen Fehler!", "ai"));
                }

                this.conversationIdleft = data1.conversationId;
                this.conversationIdright = data2.conversationId;
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