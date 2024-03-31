
// handels all the chat logic in the Q&A UI
class ChatService {

    constructor() {
        this.messages = [];
        this.conversationIdleft = null;
        this.conversationIdright = null;
        this.uiService = new UIService();
        this.ratingService = new RatingService();
        this.markdownParser = new MarkdownParser();
    }

    // adds a message to the list of messages and updates the UI
    addMessage(message) {
        this.messages.push(message);
        this.addMessagesToUI()
    }

    // can be used to start a normal chat without the evaluation. calls the leftService
    async getAnswer(requestText) {
        const displayedText = this.encodeHTML(requestText);
        this.addMessage(new Message(null, displayedText, "user"));

        try {
            const response = await fetch(
                `/llm/leftService?${this.conversationIdleft ? `conversationId=${this.conversationIdleft}` : ''}`,
                {
                    method: 'POST',
                    body: JSON.stringify({ message: requestText }),
                });

            const text = await response.text();
            console.log('Raw response:', text);

            try {
                const data = JSON.parse(text);
                if (data.answer){
                    this.addMessage(new Message(data.answerId, data.answer, "ai"));
                }
                else{
                    this.addMessage(new ErrorMessage(null, "Sorry, es gab einen Fehler. Bitte versuche es später nochmal!", "ai"));
                }
                this.conversationIdleft = data.conversationId;
            } catch (parseError) {
                console.error('Error parsing JSON:', parseError);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }

    // can be used to start a dual chat with the evaluation. calls the leftService and the rightService in parallel
    // and adds the messages to the list of messages and updates the UI
    async getDualAnswer(requestText, ratingHint) {
        this.addMessage(new Message(null, requestText, "user"));
        this.addMessage(new LoadingMessage());

        try {
            const url1 = `/llm/leftService?${this.conversationIdleft ? `conversationId=${this.conversationIdleft}` : ''}`;
            const url2 = `/llm/rightService?${this.conversationIdright ? `conversationId=${this.conversationIdright}` : ''}`;

            const fetch1 = fetch(url1, { method: 'POST', body: JSON.stringify({ message: requestText })});
            const fetch2 = fetch(url2, { method: 'POST', body: JSON.stringify({ message: requestText })});

            const [response1, response2] = await Promise.all([fetch1, fetch2]);

            const text1 = await response1.text();
            const text2 = await response2.text();
            console.log('Raw response1:', text1);
            console.log('Raw response2:', text2);

            try {
                const data1 = JSON.parse(text1);
                const data2 = JSON.parse(text2);

                if (data1.answer && data2.answer) {
                    this.messages.pop();
                    document.getElementById("loading-message").remove();
                    data1.answer = this.markdownParser.parse(data1.answer);
                    data2.answer = this.markdownParser.parse(data2.answer);
                    const messageLeft = new Message(data1.answerId, data1.answer, "ai");
                    const messageRight = new Message(data2.answerId, data2.answer, "ai");
                    const dualMessage = new DualMessage(messageLeft, messageRight);
                    this.addMessage(dualMessage);
                    this.addMessage(ratingHint);
                    ratingHint.setVisible(true);
                    this.ratingService.addRatingListener(dualMessage);
                } else {
                    this.messages.pop();
                    document.getElementById("loading-message").remove();
                    this.addMessage(new ErrorMessage(null, "Sorry, es gab einen Fehler!", "ai"));
                    document.getElementById("input").disabled = false;
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

    // adds all messages to the UI
    addMessagesToUI() {
        this.messages.forEach(message => {
            this.uiService.addMessage(message);
        });
    }

    // deletes all messages from the UI and activates the input field
    clearMessages() {
        this.messages = [];
        this.conversationIdleft = null;
        this.conversationIdright = null;
        this.uiService.clearMessages();
        document.getElementById("input").disabled = false;
    }

    // Diese Methode überprüft den Input auf HTML-Code und ersetzt ihn durch HTML-Entities um XSS-Angriffe zu verhindern
    encodeHTML(str){
        return str.replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;')
            .replace(/`/g, '&#96;');
    }
}