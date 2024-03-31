
// TODO: add progress bar
// This class displays a quiz with multiple choice questions and is a child of the Message class
// It also contains the logic for displaying the next and previous question buttons of the MultipleChoiceQuestion class
class MultipleChoiceQuizMessage extends Message {
    constructor(quiz) {
        super(quiz.id, "Hier ist dein Quiz😎. Wenn du alle Fragen beantwortet hast, können wir gerne deine Ergebnisse besprechen.", 'ai');
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

    // creates the container for the questions
    createQuestionContainer() {
        const questionContainer = document.createElement('div');
        questionContainer.className = 'questions';
        return questionContainer;
    }

    // adds a question to the container and adds an event listener to the next and previous question buttons
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

    // displays the first question
    display() {
        this.questionElements[0].element.style.display = 'flex';
    }

    // displays the next question (logic for the next Button)
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

    // displays the previous question (logic for the previous Button)
    displayPreviousQuestion(questionElement) {
        const index = this.questionElements.indexOf(questionElement);
        if (index > 0) {
            questionElement.element.style.display = 'none';
            this.questionElements[index - 1].element.style.display = 'flex';
        }
    }

    // gets called when the end of the quiz is reached, calls the showResults
    // function and adds the QuizEnd class to the questionElements array
    end() {
        this.endOfQuiz = true;
        const quizEnd = new QuizEnd(this.id);
        this.questionElements.push(quizEnd);
        this.questionContainer.appendChild(quizEnd.element);

        this.showResults();
    }

    // shows the results
    showResults() {
        this.questionElements.forEach(element => {
            if (element.element.classList.contains('right')) {
                this.score++;
                element.element.querySelector('.correct').style.backgroundColor = 'var(--correct)';
                element.element.querySelector('.correct').style.color = '#000';
            }
            else if (element.element.classList.contains('wrong')) {
                element.element.querySelector('.clicked').style.backgroundColor = 'var(--wrong)';
                element.element.querySelector('.correct').style.border = '1px solid var(--correct)';
            }
        })
    }
}