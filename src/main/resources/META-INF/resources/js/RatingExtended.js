/*
    * Diese Klasse erweitert das Rating-System um die Möglichkeit, die bevorzugte Bewertung zu spezifizieren.
*/
class RatingExtended{
    constructor(id) {
        this.id = id;
        this.element = document.createElement('div');
        this.element.className = `rating-extended`;
        this.element.innerHTML = `
            <div class="radio-rating">
                <p>Anhand welches Kriteriums bevorzugst du diese Antwort?</p>
                <div>
                <label for="inhaltlich">Inhaltlich</label>
                <input class="radio-button" type="radio" name="rating" id="inhaltlich">
                </div>
                <div>
                <label for="sprachlich">Sprachlich</label>
                <input class="radio-button" type="radio" name="rating" id="sprachlich">
                </div>
                <div>
                <label for="laenge">Antwortlänge</label>
                <input class="radio-button" type="radio" name="rating" id="laenge">
                </div>
                <div>
                <label for="sonstiges">Sonstiges</label>
                <input class="radio-button" type="radio" name="rating" id="sonstiges">
                </div>
            </div>
        `;

        this.element.querySelectorAll('input[type=radio]').forEach(input => {
            input.addEventListener('click', () => this.sendRating(input.id));
        });
    }

    sendRating(rating) {

        fetch('/api/answer/preffered/cause?id=' + this.id + '&value=' + rating, {
            method: 'PUT',
        }).catch(error => {
            console.error('Error:', error);
        });

        // Löscht die Rating-UI, aktiviert das Input-Feld und setzt den Cursor auf das Input-Feld
        this.element.remove();
        document.getElementById("input").disabled = false;
        document.getElementById("input").focus();
    }
}