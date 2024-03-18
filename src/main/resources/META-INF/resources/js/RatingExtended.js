class RatingExtended{
    constructor(id) {
        this.id = id;
        this.element = document.createElement('div');
        this.element.className = `rating-extended`;
        this.element.innerHTML = `
                                    <div class="radio-rating">
                                        <p>Anhand welches Kriteriums bevorzugst du diese Antwort?</p>
                                        <label for="Inhaltlich">Inhaltlich</label>
                                        <input type="radio" name="Inhaltlich" id="inhaltlich" onclick="this.sendRating('inhaltlich')">
                                        <label for="Sprachlich">Sprachlich</label>
                                        <input type="radio" name="Sprachlich" id="sprachlich" onclick="this.sendRating('sprachlich')">
                                        <label for="Sonstiges">Sonstiges</label>
                                        <input type="radio" name="Sonstiges" id="sonstiges" onclick="this.sendRating('sonstiges')">
                                    </div>
                                `;
        
    }

    sendRating(rating) {

        fetch('/api/answer/preffered/cause?id=' + this.id + '&value=' + rating, {
            method: 'PUT',
        }).catch(error => {
            console.error('Error:', error);
        });
    }
}