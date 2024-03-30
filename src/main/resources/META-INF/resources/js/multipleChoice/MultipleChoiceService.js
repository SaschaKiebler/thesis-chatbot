
class MultipleChoiceService {

    constructor() {
        this.conversationId = null;
    }

    async getAnswer(message) {
            const body = {
                message: message
            };

        return await fetch(`/api/multipleChoice?conversationId=${this.conversationId ? this.conversationId : ""}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(message)
        }).then(
            data => {
                return data.json();
            }
        ).then(
            data => {
                this.conversationId = data.conversationId;
                console.log(data);
                return data;
            }
        ).catch(error => {
            console.error('Error:', error);
        });
    }


    async getScripts(lectureId) {
        return await fetch(`/api/lectures/${lectureId}/scripts`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(data => {
            return data.json();
        }).catch(error => {
            console.error('Error:', error);
        });
    };


}