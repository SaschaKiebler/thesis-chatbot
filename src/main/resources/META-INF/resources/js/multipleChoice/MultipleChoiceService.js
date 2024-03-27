
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
                return data.answer;
            }
        ).catch(error => {
            console.error('Error:', error);
        });
    }

    getScriptsFromLecture(lectureId) {
        return fetch(`/api/lectures/${lectureId}/scripts`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(
            data => {
                return data.json();
            }
        ).then(
            data => {
            console.log(data);
            return data;
        }).catch(error => {
            console.error('Error:', error);
        });
    }
}