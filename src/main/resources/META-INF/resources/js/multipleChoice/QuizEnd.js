
class QuizEnd {

    constructor(id) {
        this.element = document.createElement('div');
        this.element.className = 'quiz-end';
        this.questionElements = [];
        this.element.innerHTML = `
            <div class="text">
                <p>Deine Ergebnisse:</p>
            </div>
            <div class="questions"></div>
            <div class="progress"></div>
        `;
        this.questionContainer = this.element.querySelector('.questions');
        this.progress = this.element.querySelector('.progress');
        this.score = 0;
        this.id = id;
        this.display();
    }

    getQuestionElements() {
        document.querySelectorAll('.question').forEach(element => {
            this.questionElements.push(element);
        })
    }

    display() {
        this.getQuestionElements();
        this.questionElements.forEach(element => {
            if (element.classList.contains('right')) {
                this.score++;
            }
            const result = document.createElement('div');
            result.className = 'question-result';
            result.innerHTML = this.questionElements.indexOf(element) + 1;
            if (element.classList.contains('right')) {
                result.style.backgroundColor = '#00ff00';
            } else {
                result.style.backgroundColor = '#ff0000';
            }
            this.questionContainer.appendChild(result);
        });
    }
}