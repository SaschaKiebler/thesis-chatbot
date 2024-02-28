
/*
    Diese Klasse manipuliert die UI.
 */
class UIService {
    constructor() {
        this.conversation = document.getElementById('conversation');
    }

    // Fügt Nachrichten der UI hinzu
    addMessage(message) {
        this.conversation.appendChild(message.element);
    }

    // Entfernt alle Nachrichten aus der UI und fügt eine Begrüßungsnachricht hinzu
    clearMessages() {
        this.conversation.innerHTML = '';
        this.addMessage(new Message(null, "Hallo! Wie kann ich dir in den Themen Gesundheitsinformatik, Telemedizin und dem deutschen Gesundheitswesen behilflich sein?", "ai"));
    }

    // Gibt der UI die Möglichkeit den Dark-Mode zu aktivieren und fügt die CSS Klasse hinzu oder entfernt sie
    // Sie ändert auch das Bild des Dark-Mode Buttons
    toggleDarkMode() {
        document.body.classList.toggle('dark-mode');
        // change image in darkmode button
        document.getElementById('darkmode').firstChild.src = document.body.classList.contains('dark-mode') ? 'pictures/lightmode.svg' : 'pictures/darkmode.svg';
    }

    enablePopUp() {
        document.addEventListener("DOMContentLoaded", function() {
            const input = document.getElementById("input");
            const popup = document.getElementById("popup");

            function togglePopup() {
                if (input.disabled) {
                    popup.classList.add("show");
                } else {
                    popup.classList.remove("show");
                }
            }

            // Event listener for mouseenter and mouseleave
            input.addEventListener("mouseenter", togglePopup);
            input.addEventListener("mouseleave", function() {
                popup.classList.remove("show");
            });
        });

    }





}