
class MultipleChoiceService {

    constructor() {
        this.conversationId = null;
    }

    async startTheQuizChain(message) {
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
            return {"answer": "etwas is schief gelaufen... bitte drücke auf clear und versuche es erneut."}
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

    async sendResults(quizId, answers) {
        const body = { conversationId: this.conversationId, results: answers, quizId: quizId };
        return await fetch(`/api/quiz/result`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body)
        }).then(
            data => {
                console.log(data);
                return data.json();
            }
        ).catch(error => {
            console.error('Error:', error);
        })
    }

    async talkAboutResults(userInput) {
        const body = { conversationId: this.conversationId, message: userInput };
        return await fetch(`/api/quiz/talk`, {
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
                console.log(data);
                return data;
            }
        ).catch( error => {
            console.error('Error:', error);
        })
    }


}