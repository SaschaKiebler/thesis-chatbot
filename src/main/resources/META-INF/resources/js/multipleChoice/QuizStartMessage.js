
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
            this.startQuiz(id);
        })
        buttonContainer.appendChild(button);
        text.appendChild(buttonContainer);
    }

    // Fragt das Quiz mit der Id an
    async startQuiz(id) {
        const response = await fetch(`/api/quiz/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        console.log(data);
        if (data.success) {

        }

    }
}