
class QuizStartMessage extends Message{
    constructor(id, uiService) {
        super(id, "Willkommen zum Multiple Choice Quiz", "ai");
        const text = this.element.querySelector('.text');
        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'start-quiz';
        const button = document.createElement('button');
        button.className = 'start-quiz-button';
        button.innerText = 'Starten';
        button.addEventListener('click', () => {
            this.startQuiz(id);
        })
        buttonContainer.appendChild(button);
        text.appendChild(buttonContainer);

        this.MultipleChoiceService = new MultipleChoiceService();
        this.uiService = uiService;

    }

    // Fragt das Quiz mit der Id an von Service an und füge es als MultipleChoiceQuizMessage der UI hinzu
    async startQuiz(id) {
        this.element.querySelector('.start-quiz-button').disabled = true;
        try {


        const quiz = await this.MultipleChoiceService.getQuiz(id);
        if (quiz.questions.length === 0) {
            this.uiService.addMessage(new Message(null, "Leider konnten wir deine Quizdaten nicht laden. Bitte versuche es erneut.", 'ai'));
        }
        else {
        this.uiService.addMessage(new MultipleChoiceQuizMessage(quiz));
        }
        }
        catch(error) {
            this.uiService.addMessage(new Message(null, "Leider konnten wir deine Quizdaten nicht laden. Bitte versuche es erneut.", 'ai'));
        }
    }
}