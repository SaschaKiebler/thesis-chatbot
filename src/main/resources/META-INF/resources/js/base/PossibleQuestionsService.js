class PossibleQuestionsService {

    constructor(input) {
        this.input = input;
    }

    addPossibleQuestions(message, possibleQuestions) {
        const possibleQuestionsDiv = document.createElement('div');
        possibleQuestionsDiv.className = 'possible-questions';



        for (const possibleQuestion of possibleQuestions) {
            const button = document.createElement('button');
            button.innerText = possibleQuestion;
            button.className = 'possible-question-button option';

           possibleQuestionsDiv.appendChild(button);
        }
        message.element.querySelector('.message-content').lastChild.after(possibleQuestionsDiv);
    }




    removePossibleQuestionsEventListeners(){
        const possibleQuestions = document.querySelector('.possible-questions');
        if(possibleQuestions){
            possibleQuestions.remove();
        }
    }

    addChosenQuestionToInput(chosenQuestion){
        this.input.value = chosenQuestion;
    }
}