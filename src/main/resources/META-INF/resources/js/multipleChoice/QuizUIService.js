
// TODO disable the inputfield before the end of the quiz
// manages the UI for the multiple choice quiz
class QuizUIService {
    constructor(uiService, multipleChoiceService) {
        this.uiService = uiService;
        this.multipleChoiceService = multipleChoiceService;
        this.initializeWelcomeMessage();
        this.markdownParser = new MarkdownParser();
    }

    // initializes the welcome message
    initializeWelcomeMessage() {
        // const welcomeMessage = new OptionMessage(null, "Hallo! Toll, dass du hier bist 🤩. Ich kann dir beim Lernen helfen. Wenn du mir eine URL zu deinem Thema oder direkt den Text gibst, kann ich dir ein Multiple-Choice-Quiz erstellen.", this.lectures.map(lecture => ({ name: lecture.name, id: lecture.id })), "lectures");
        const welcomeMessage = new Message(null, "Hallo! Toll, dass du hier bist 🤩. Ich kann dir beim Lernen helfen. Wenn du mir eine URL zu deinem Thema oder direkt den Text gibst, kann ich dir ein Multiple-Choice-Quiz erstellen.", "ai");
        this.uiService.clearMessages(welcomeMessage);
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

    // function to handle a request and either display a normal ai message or a start quiz message
    async handleRequest(input) {
        const inputText = this.sanitizeString(input);
        this.displayUserMessage(inputText);
        const loadingMessageId = this.displayLoadingMessage("");

        const answer = await this.multipleChoiceService.startTheQuizChain(input);
        console.log(answer);
        this.uiService.removeMessage(loadingMessageId);
        const message = answer.quizId ? new QuizStartMessage(answer.quizId, this.uiService, this.multipleChoiceService) : new Message(null, this.markdownParser.parse(answer.message), 'ai');
        this.uiService.addMessage(message);
        // check if there are possible follow-up questions and add them to the message
        if (answer.possibleFollowupQuestions) {
            const possQService = new PossibleQuestionsService(document.querySelector('#input'));
            possQService.addPossibleQuestions(message, answer.possibleFollowupQuestions);
            possQService.addClickListenerToPossibleQuestions();

        }
        return answer;
    }

    // Verhindert XSS
    sanitizeString(str) {
        return str.replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#39;")
            .replace(/`/g, "&#96;");
    }




    // Ab hier alter code für feste Datenbasis, momentan nicht in Benutzung
    // TODO: remove this code


    // function to disable the input options
    disableInputOptions() {
        const inputOptions = document.getElementsByClassName('option');
        for (const inputOption of inputOptions) {
            inputOption.disabled = true;
        }
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
        const loadingMessageId = this.displayLoadingMessage("erzeugt ein Quiz zum Skript " + scriptOption.name);

        const answer = await this.multipleChoiceService.startTheQuizChain("erzeuge ein Quiz zum Skript " + scriptOption.id);
        this.uiService.removeMessage(loadingMessageId);
        const message = answer.quizId ? new QuizStartMessage(answer.quizId, this.uiService, this.multipleChoiceService) : new Message(null, answer.answer, 'ai');
        this.uiService.addMessage(message);
    }
}

