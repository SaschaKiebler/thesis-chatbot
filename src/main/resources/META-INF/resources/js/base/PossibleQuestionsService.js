class PossibleQuestionsService {

    constructor(input) {
        this.input = input;
    }

    addPossibleQuestions(message, possibleQuestions) {
        const possibleQuestionsDiv = document.createElement('div');
        possibleQuestionsDiv.className = 'possible-questions';



        for (const possibleQuestion of possibleQuestions) {
            if (possibleQuestion !== "" && possibleQuestion.length > 0) {
            const button = document.createElement('button');
            button.innerText = possibleQuestion;
            button.className = 'possible-question-button option';

           possibleQuestionsDiv.appendChild(button);
            }
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

    addClickListenerToPossibleQuestions(){
        const possibleQuestions = document.querySelectorAll('.possible-question-button');
        for(const possibleQuestion of possibleQuestions){
            possibleQuestion.addEventListener('click', () => {
                this.addChosenQuestionToInput(possibleQuestion.innerText);
                document.querySelector('#send').click();
            });
        }
    }
}