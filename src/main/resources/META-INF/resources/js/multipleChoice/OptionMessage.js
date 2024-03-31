
// Represents a message with multiple options
class OptionMessage extends Message{

    constructor(id, message, options, type) {
        super(id, message, 'ai');
        this.options = options;
        console.log(this.options);
        this.createOptions();
        this.element.classList.add(type + '-message');
    }

    // Creates the buttons for the options
    createOptions() {
        const text = this.element.querySelector('.text');
        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'options';
        this.options.forEach(option => {
            const button = document.createElement('button');
            button.className = 'option';
            button.innerHTML = option.name;
            button.id = option.id;
            buttonContainer.appendChild(button);
            text.appendChild(buttonContainer);
        });
    }

}