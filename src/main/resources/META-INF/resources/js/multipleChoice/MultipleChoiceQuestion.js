
class MultipleChoiceQuestion {

    constructor(id, question, answers) {
        this.question = question;
        this.answers = answers;
        this.element = document.createElement('div');
        this.element.className = 'question';
        this.element.id = id;
        this.element.innerHTML = `
            <p>${this.question}</p>
            <div class="answers">
            </div>
        `;
        for (const answer of this.answers) {
            this.addAnswer(answer);
        }
    }

    addAnswer(answer) {
        const answerElement = document.createElement('div');
        answerElement.className = 'answer';
        answerElement.innerHTML = answer;
        this.element.querySelector('.answers').appendChild(answerElement);
    }
}