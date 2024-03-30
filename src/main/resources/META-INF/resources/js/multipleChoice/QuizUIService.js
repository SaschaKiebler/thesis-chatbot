class QuizUIService {
    constructor(uiService, multipleChoiceService, lectures) {
        this.lectures = lectures;
        this.uiService = uiService;
        this.multipleChoiceService = multipleChoiceService;
        this.initializeWelcomeMessage();
    }

    initializeWelcomeMessage() {
        const welcomeMessage = new OptionMessage(null, "Hallo! Toll, dass du hier bist 🤩. Zu welchem Fach möchtest du ein Quiz durchführen?", this.lectures.map(lecture => ({ name: lecture.name, id: lecture.id })), "lectures");
        this.uiService.clearMessages(welcomeMessage);
        this.initializeLectureSelection();
    }

    initializeLectureSelection() {
        const lectureOptions = document.querySelector('.lectures-message').getElementsByClassName('option');
        Array.from(lectureOptions).forEach(lecture => {
            lecture.addEventListener('click', () => this.selectLecture(lecture));
        });
    }

    async selectLecture(lecture) {
        this.displayUserMessage(lecture.innerHTML);
        const loadingMessageId = this.displayLoadingMessage();
        this.disableInputOptions();

        const scripts = await this.multipleChoiceService.getScripts(lecture.id);
        this.uiService.removeMessage(loadingMessageId);

        const scriptMessage = new OptionMessage(null, "Bitte wähle ein Script aus.", scripts, "scripts");
        this.uiService.addMessage(scriptMessage);
        this.initializeScriptSelection(scriptMessage);
    }

    displayLoadingMessage() {
        const loadingMessage = new LoadingMessage();
        this.uiService.addMessage(loadingMessage);
        return loadingMessage.id;
    }

    displayUserMessage(message) {
        const userMessage = new Message(null, message, 'user');
        this.uiService.addMessage(userMessage);
    }

    initializeScriptSelection(scriptMessage) {
        const scriptOptions = scriptMessage.element.getElementsByClassName('option');
        Array.from(scriptOptions).forEach(scriptOption => {
            scriptOption.addEventListener('click', () => this.selectScript(scriptOption));
        });
    }

    async selectScript(scriptOption) {
        this.disableInputOptions();
        this.displayUserMessage(scriptOption.innerHTML);
        const loadingMessageId = this.displayLoadingMessage();

        const answer = await this.multipleChoiceService.getAnswer("erzeuge ein Quiz zum Skript " + scriptOption.id);
        this.uiService.removeMessage(loadingMessageId);

        const message = answer.quizId ? new QuizStartMessage(answer.quizId, this.uiService) : new Message(null, answer.answer, 'ai');
        this.uiService.addMessage(message);
    }

    disableInputOptions() {
        const inputOptions = document.getElementsByClassName('option');
        for (const inputOption of inputOptions) {
            inputOption.disabled = true;
        }
    }
}

