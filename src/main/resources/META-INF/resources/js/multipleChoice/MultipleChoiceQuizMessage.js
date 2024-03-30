
class MultipleChoiceQuizMessage extends Message {
    constructor(quiz) {
        super(quiz.id, "Beantworte die Fragen, im Anschluss können wir über die Lösungen sprechen.", 'ai');
        const text = this.element.querySelector('.text');
        this.questionContainer = this.createQuestionContainer();
        for (const question of quiz.questions) {
            this.addQuestion(question);
        }
        text.appendChild(this.questionContainer);

        this.quiz = quiz;
    }

    createQuestionContainer() {
        const questionContainer = document.createElement('div');
        questionContainer.className = 'questions';
        return questionContainer;
    }

    addQuestion(question) {
        const questionElement = new MultipleChoiceQuestion(question.id, question.question, question.answers);
        this.questionContainer.appendChild(questionElement.element);
    }


}