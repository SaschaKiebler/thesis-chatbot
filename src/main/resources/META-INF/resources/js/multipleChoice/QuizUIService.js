
// TODO disable the inputfield before the end of the quiz
// manages the UI for the multiple choice quiz
class QuizUIService {
    constructor(uiService, multipleChoiceService, lectures) {
        this.lectures = lectures;
        this.uiService = uiService;
        this.multipleChoiceService = multipleChoiceService;
        this.initializeWelcomeMessage();
    }

    // initializes the welcome message
    initializeWelcomeMessage() {
        const welcomeMessage = new OptionMessage(null, "Hallo! Toll, dass du hier bist 🤩. Zu welchem Fach möchtest du ein Quiz durchführen?", this.lectures.map(lecture => ({ name: lecture.name, id: lecture.id })), "lectures");
        this.uiService.clearMessages(welcomeMessage);
        this.initializeLectureSelection();
    }

    // initializes the event listener for each lecture in the message
    initializeLectureSelection() {
        const lectureOptions = document.querySelector('.lectures-message').getElementsByClassName('option');
        Array.from(lectureOptions).forEach(lecture => {
            lecture.addEventListener('click', () => this.selectLecture(lecture));
        });
    }

    // initializes the event listener for each script in the message
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

    // function to display a loading message
    displayLoadingMessage(message) {
        const loadingMessage = new LoadingMessage(message);
        this.uiService.addMessage(loadingMessage);
        return loadingMessage.id;
    }

    // function to display a user message
    displayUserMessage(message) {
        const userMessage = new Message(null, message, 'user');
        this.uiService.addMessage(userMessage);
    }

    // initializes the event listener for each script-option in the message
    initializeScriptSelection(scriptMessage) {
        const scriptOptions = scriptMessage.element.getElementsByClassName('option');
        Array.from(scriptOptions).forEach(scriptOption => {
            scriptOption.addEventListener('click', () => this.selectScript(scriptOption));
        });
    }

    // function that handles the selection of a script
    async selectScript(scriptOption) {
        this.disableInputOptions();
        this.displayUserMessage(scriptOption.innerHTML);
        const loadingMessageId = this.displayLoadingMessage("erzeuge ein Quiz zum Skript " + scriptOption.name);

        const answer = await this.multipleChoiceService.getAnswer("erzeuge ein Quiz zum Skript " + scriptOption.id);
        this.uiService.removeMessage(loadingMessageId);

        const message = answer.quizId ? new QuizStartMessage(answer.quizId, this.uiService) : new Message(null, answer.answer, 'ai');
        this.uiService.addMessage(message);
    }

    // function to disable the input options
    disableInputOptions() {
        const inputOptions = document.getElementsByClassName('option');
        for (const inputOption of inputOptions) {
            inputOption.disabled = true;
        }
    }
}

