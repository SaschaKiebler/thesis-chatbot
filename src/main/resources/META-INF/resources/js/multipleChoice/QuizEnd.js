/**
 * TODO options how to continue
 * TODO progress bar
 *
 * Represents the end of a quiz. Contains the results of the quiz and the options how to continue.
 */
class QuizEnd {

    constructor(id, multipleChoiceService) {
        this.element = document.createElement('div');
        this.element.className = 'quiz-end';
        this.questionElements = [];
        this.element.innerHTML = `
            <div class="text">
                <p>Deine Ergebnisse:</p>
            </div>
            <div id="question-result-container" class="questions"></div>
            <div class="progress"></div>
            <button id="talk-about-results">Ergebnisse besprechen</button>
            <button id="new-quiz">neues Quiz</button>
        `;
        this.questionContainer = this.element.querySelector('.questions');
        this.progress = this.element.querySelector('.progress');
        this.score = 0;
        this.id = id;
        this.display();
        this.questionResultContainer = this.element.querySelector('.question-result-container');
        this.multipleChoiceService = multipleChoiceService;
        this.uIService = new UIService();
        this.markdownParser = new MarkdownParser();


        this.addEventListenerToTalkAboutResults();
    }

    // get all question elements from DOM
    getQuestionElements() {
        document.querySelectorAll('.question').forEach(element => {
            this.questionElements.push(element);
        })
    }

    // display results and mark the answers in the questions
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
                result.style.backgroundColor = 'var(--correct)';
            } else {
                result.style.backgroundColor = 'var(--wrong)';
            }
            this.questionContainer.appendChild(result);
        });
    }

    addEventListenerToTalkAboutResults() {
        this.element.querySelector('#talk-about-results').addEventListener('click', async () => {
            const answers = [];
            document.querySelectorAll('.clicked').forEach(element => {
                answers.push(element.id);
            });
            console.log(answers);
            this.uIService.addMessage(new LoadingMessage("wird gesendet..."));
            await this.multipleChoiceService.sendResults(this.id, answers);
            this.uIService.removeMessage("loading-message");
            this.uIService.addMessage(new Message(null, "Was möchtest du besprechen?", 'ai'));
            document.querySelector('#input').disabled = false;
        });
    }

}