
class MultipleChoiceQuestion {

    constructor(id, question, answers) {
        this.question = question;
        this.answers = answers;
        this.element = document.createElement('div');
        this.element.className = 'question';
        this.element.id = id;
        this.element.innerHTML = `
            <div class="progress"></div>
            <p>${this.question}</p>
            <div class="answers">
            </div>
            <div class="next-question">
                <button class="previous-question-button question-control-buttons">Zurück</button>
                <button class="next-question-button question-control-buttons">Weiter</button>
            </div>
        `;
        for (const answer of this.answers) {
            this.addAnswer(answer);
        }
        this.addAnswerListener();
    }

    addAnswer(answer) {
        const answerElement = document.createElement('button');
        answerElement.className = 'answer';
        answerElement.id = answer.id;
        answerElement.innerHTML = `${answer.answer}`;
        if (answer.correct) {
            answerElement.classList.add('correct');
        }
        this.element.querySelector('.answers').appendChild(answerElement);
    }

    addAnswerListener() {
        this.element.querySelectorAll('.answers .answer').forEach(button => {
            button.addEventListener('click', () => {
                if (this.element.querySelector('.clicked')) {
                    this.element.querySelector('.clicked').classList.remove('clicked');
                }
                if(!button.classList.contains('clicked')) {
                    button.classList.add('clicked');
                    this.element.classList.add('answered');
                }
                if (button.classList.contains('correct')) {
                    this.element.classList.add('right');
                    this.element.classList.remove('wrong');
                }
                if (!button.classList.contains('correct')) {
                    this.element.classList.add('wrong');
                    this.element.classList.remove('right');
                }
            });
        });
    }
}