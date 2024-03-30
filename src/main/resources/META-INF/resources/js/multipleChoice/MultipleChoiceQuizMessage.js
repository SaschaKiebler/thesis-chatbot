
class MultipleChoiceQuizMessage extends Message {
    constructor(quiz) {
        super(quiz.id, "Beantworte die Fragen, im Anschluss können wir über die Lösungen sprechen.", 'ai');
        const text = this.element.querySelector('.text');
        this.questionElements = [];
        this.questionContainer = this.createQuestionContainer();
        this.quiz = quiz;
        for (const question of quiz.questions) {
            this.addQuestion(question);
        }
        text.appendChild(this.questionContainer);
        this.display();
        this.endOfQuiz = false;
    }

    createQuestionContainer() {
        const questionContainer = document.createElement('div');
        questionContainer.className = 'questions';
        return questionContainer;
    }

    addQuestion(question) {
        const questionElement = new MultipleChoiceQuestion(question.id, question.question, question.answers);
        this.questionContainer.appendChild(questionElement.element);
        console.log(questionElement);
        this.questionElements.push(questionElement);
        questionElement.element.querySelector('.next-question-button').addEventListener('click', () => {
            if (questionElement.element.classList.contains('answered')){
                this.displayNextQuestion(questionElement);
            }
        });
        questionElement.element.querySelector('.previous-question-button').addEventListener('click', () => {
                this.displayPreviousQuestion(questionElement);

        })
    }

    display() {
        this.questionElements[0].element.style.display = 'flex';
    }


    displayNextQuestion(questionElement) {
        const index = this.questionElements.indexOf(questionElement);
        if (index < this.questionElements.length - 1 && !this.endOfQuiz) {
            questionElement.element.style.display = 'none';
            this.element.querySelector('.progress').style.width = `${(index + 1) * 100 / this.questionElements.length}%`;
            this.questionElements[index + 1].element.style.display = 'flex';

        }
        else if (this.endOfQuiz && index < this.questionElements.length - 2) {
            questionElement.element.style.display = 'none';
            this.questionElements[index + 1].element.style.display = 'flex';

        }
        else if (!this.endOfQuiz) {
            this.end();
        }
    }

    displayPreviousQuestion(questionElement) {
        const index = this.questionElements.indexOf(questionElement);
        if (index > 0) {
            questionElement.element.style.display = 'none';
            this.questionElements[index - 1].element.style.display = 'flex';
        }
    }

    end() {
        this.endOfQuiz = true;
        const quizEnd = new QuizEnd(this.id);
        this.questionElements.push(quizEnd);
        this.questionContainer.appendChild(quizEnd.element);

        this.showResults();
    }

    showResults() {
        this.questionElements.forEach(element => {
            if (element.element.classList.contains('right')) {
                this.score++;
                element.element.querySelector('.correct').style.backgroundColor = '#00ff00';
            }
            else if (element.element.classList.contains('wrong')) {
                element.element.querySelector('.clicked').style.backgroundColor = '#ff0000';
                element.element.querySelector('.correct').style.border = '1px solid #00ff00';
            }
        })
    }
}