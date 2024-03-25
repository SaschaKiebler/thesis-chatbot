
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
    clearMessages(message) {
        this.conversation.innerHTML = '';
        this.addMessage(message ? message : new Message(null, "Hallo! Wie kann ich dir in den Themen Gesundheitsinformatik, Telemedizin und dem deutschen Gesundheitswesen behilflich sein?", "ai"));
    }

    // Gibt der UI die Möglichkeit den Dark-Mode zu aktivieren und fügt die CSS Klasse hinzu oder entfernt sie
    // Sie ändert auch das Bild des Dark-Mode Buttons
    toggleDarkMode() {
        document.body.classList.toggle('dark-mode');
        document.getElementById('darkmode').firstChild.src = document.body.classList.contains('dark-mode') ? 'pictures/lightmode.svg' : 'pictures/darkmode.svg';
    }

    // Aktiviert den Pop-Up Text, wenn das Input-Feld deaktiviert ist
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

            // Event listener für mouseenter und mouseleave um das Pop-Up zu aktivieren/deaktivieren
            input.addEventListener("mouseenter", togglePopup);
            input.addEventListener("mouseleave", function() {
                popup.classList.remove("show");
            });
        });

    }

    // Speichert den Dark-Mode in den Local Storage auf Client
    saveDarkMode() {
        if (document.body.classList.contains('dark-mode')) {
            localStorage.setItem('dark-mode', 'true');
        } else {
            localStorage.setItem('dark-mode', 'false');
        }
    }

    // Lädt den Dark-Mode aus dem Local Storage beim Laden der Seite
    loadDarkMode() {
        if (localStorage.getItem('dark-mode') === 'true') {
            document.body.classList.add('dark-mode');
        }
    }

    // löscht Nachrichten aus der UI
    removeMessage(id) {
        const message = document.getElementById(id);
        message.remove();
    }


}