
class MultipleChoiceService {

    constructor() {
        this.conversationId = null;
    }

    async getAnswer(message) {
        const body = {
            message: message,
            conversationId: (this.conversationId ? this.conversationId : "")
        }
        return await fetch(`/api/quizChain`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body)
        }).then(
            data => {
                return data.json();
            }
        ).then(
            data => {
                this.conversationId = data.conversationId;
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

    async getQuiz(quizId) {
        return await fetch(`/api/quiz/${quizId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(data => {
            return data.json();
        }).then(
            data => {
                if (data.details){
                    throw new Error(data.details);
                }
                return data;
            }
        ).catch(error => {
            console.error('Error:', error);
            throw new Error(error);
        });

    }


}