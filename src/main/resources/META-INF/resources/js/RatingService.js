class RatingService {
    constructor() {
        this.messages = [];
    }

    addRatingListener() {
        this.messages = document.getElementsByClassName("ai-message");
        for (let message of this.messages) {
            if (message.id) {
                message.addEventListener('click', (event) => {
                    this.sendRating(message);
                });
            }
        }
    }

    async sendRating(message) {
        // Send the rating to the server
        message.classList.add("rated");
        message.parentElement.classList.add("rating-disabled");

        console.log("Sending rating for message " + message.id);
        await fetch('/answer/preffered?id=' + message.id + '&value=true', {
            method: 'PUT',
        }).catch(error => {
            console.error('Error:', error);
        });
        console.log("Rating sent");
        this.setRatingDisabled();
    }

    setRatingDisabled(){
        const messages = document.getElementsByClassName("rating-disabled");
        for (let message of messages) {
            message.replaceWith(message.cloneNode(true));
        }
    }

}