
class QuizStartMessage extends Message{
    constructor(id) {
        super(id, "Willkommen zum Multiple Choice Quiz", "ai");
        const text = this.element.querySelector('.text');
        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'start-quiz';
        const button = document.createElement('button');
        button.className = 'start-quiz-button';
        button.innerText = 'Starten';
        button.addEventListener('click', () => {
            this.startQuiz();
        })
        buttonContainer.appendChild(button);
        text.appendChild(buttonContainer);
    }
}