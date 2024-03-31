
/*
    Handels the rating system and sends the rating to the server
 */
class RatingService {
    constructor() {
        this.messages = [];
    }

    // adds the rating listener to the dual message
    addRatingListener(dualMessage) {
        this.messages = document.getElementById(dualMessage.id).children;
        for (let message of this.messages) {
            if (message.id) {
                message.addEventListener('click', (event) => {
                    this.sendRating(message);
                });
            }
        }
    }

    // sends the rating to the server
    async sendRating(message) {
        // Send the rating to the server
        message.classList.add("rated");
        message.parentElement.classList.add("rating-disabled");

        console.log("Sending rating for message " + message.id);
        await fetch('/api/answer/preffered?id=' + message.id + '&value=true', {
            method: 'PUT',
        }).catch(error => {
            console.error('Error:', error);
        });
        this.setRatingDisabled();
        this.addExtendedRating(message);
    }

    // removes the rating UI
    setRatingDisabled(){
        const messages = document.getElementsByClassName("rating-disabled");
        for (let message of messages) {
            for (let child of message.children) {
                child.replaceWith(child.cloneNode(true));
            }
        }
    }

    // adds the extended rating UI
    addExtendedRating(message){
        const ratingExtended = new RatingExtended(message.id);
        document.getElementById(message.id).parentElement.after(ratingExtended.element);
        document.getElementById("scroller").scrollTo(0, document.getElementById("scroller").scrollHeight);
    }

}