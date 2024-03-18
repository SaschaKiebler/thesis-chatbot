class RatingExtended{
    constructor(id) {
        this.id = id;
        this.element = document.createElement('div');
        this.element.className = `rating-extended`;
        this.element.innerHTML = `
            <div class="radio-rating">
                <p>Anhand welches Kriteriums bevorzugst du diese Antwort?</p>
                <label for="inhaltlich">Inhaltlich</label>
                <input type="radio" name="rating" id="inhaltlich">
                <label for="sprachlich">Sprachlich</label>
                <input type="radio" name="rating" id="sprachlich">
                <label for="sonstiges">Sonstiges</label>
                <input type="radio" name="rating" id="sonstiges">
            </div>
        `;

        // Adding event listeners
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

        this.element.remove();
        document.getElementById("input").disabled = false;
    }
}