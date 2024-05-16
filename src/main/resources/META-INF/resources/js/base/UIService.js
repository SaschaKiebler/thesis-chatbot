
/*
    This class provides methods to add messages to the UI and to toggle the dark mode
 */
class UIService {
    constructor() {
        this.conversation = document.getElementById('conversation');
    }

    // adds a message to the UI
    addMessage(message) {
        this.conversation.appendChild(message.element);
        // scrolle nach unten
        document.getElementById("scroller").scrollTo(0, document.getElementById("scroller").scrollHeight);
    }

    // deletes all messages from the UI and adds a new welcome message
    clearMessages(message) {
        this.conversation.innerHTML = '';
        this.addMessage(message ? message : new Message(null, "Hallo! Wie kann ich dir in den Themen Gesundheitsinformatik, Telemedizin und dem deutschen Gesundheitswesen behilflich sein?", "ai"));
        document.getElementById("input").disabled = false;
        document.getElementById("input").focus();
    }

    // function to toggle the dark mode
    // also changes the icon of the button
    toggleDarkMode() {
        document.body.classList.toggle('dark-mode');
        document.getElementById('darkmode').firstChild.src = document.body.classList.contains('dark-mode') ? 'pictures/lightmode.svg' : 'pictures/darkmode.svg';
    }

    // activates the pop up when the input is disabled
    enablePopUp() {
        document.addEventListener("DOMContentLoaded", function() {
            const input = document.getElementById("input");
            const popup = document.getElementById("popup");

            // function to toggle the popup
            function togglePopup() {
                if (input.disabled) {
                    popup.classList.add("show");
                } else {
                    popup.classList.remove("show");
                }
            }

            // Event listener for mouseenter and mouseleave to toggle the popup
            input.addEventListener("mouseenter", togglePopup);
            input.addEventListener("mouseleave", function() {
                popup.classList.remove("show");
            });
        });

    }

    // saves the dark mode in the Local Storage
    saveDarkMode() {
        if (document.body.classList.contains('dark-mode')) {
            localStorage.setItem('dark-mode', 'true');
        } else {
            localStorage.setItem('dark-mode', 'false');
        }
    }

    // Loads the dark mode from the Local Storage
    loadDarkMode() {
        if (localStorage.getItem('dark-mode') === 'true') {
            document.body.classList.add('dark-mode');
        }
    }

    // deletes the message with the given id from the UI
    removeMessage(id) {
        const message = document.getElementById(id);
        message.remove();
    }


    userPopUp() {
        if (document.getElementById("user-popup").style.display === "flex") {
            document.getElementById("user-popup").style.display = "none";
        }
        else
        document.getElementById("user-popup").style.display = "flex";
    }

    async addStudentScoresToUI(studentId, studentService) {
        const scores = await studentService.getStudentScores(studentId);
        const scoreContainer = document.getElementById("score-container");
        scoreContainer.innerHTML = "";
        scores.forEach(score => {
            const scoreElement = document.createElement("div");
            scoreElement.classList.add("score-field");
            const scoreName = document.createElement("p");
            scoreName.textContent = score.lectureName + ": ";
            scoreName.classList.add("score-name");
            scoreElement.appendChild(scoreName);
            const progressBarContainer = document.createElement("div");
            progressBarContainer.classList.add("progressbar-container");
            const progressBar = document.createElement("div");
            progressBar.classList.add("progressbar");
            progressBar.style.width = (score.result*100).toFixed(2) + "%";
            progressBar.textContent = (score.result*100).toFixed(2) + "%";
            progressBarContainer.appendChild(progressBar);
            scoreElement.appendChild(progressBarContainer);
            scoreContainer.appendChild(scoreElement);
        })
    }
}