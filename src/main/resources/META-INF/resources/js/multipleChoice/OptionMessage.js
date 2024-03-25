
class OptionMessage extends Message{

    constructor(id, message, options) {
        super(id, message, 'ai');
        this.options = options;
        this.createOptions();
    }

    createOptions() {
        const text = this.element.querySelector('.text')
        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'options';
        this.options.forEach(option => {
            const button = document.createElement('button');
            button.className = 'option';
            button.innerHTML = option;
            button.addEventListener('click', () => {
                this.sendMessage(option);
            });
            buttonContainer.appendChild(button);
            text.appendChild(buttonContainer);
        });
    }
}