
class OptionMessage extends Message{

    constructor(id, message, options) {
        super(id, message, 'ai');
        this.options = options;
        console.log(this.options);
        this.createOptions();
    }

    createOptions() {
        const text = this.element.querySelector('.text')
        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'options';
        this.options.forEach(option => {
            const button = document.createElement('button');
            button.className = 'option';
            button.innerHTML = option.name;
            button.addEventListener('click', () => {
                this.sendSelect(option.id);
            });
            buttonContainer.appendChild(button);
            text.appendChild(buttonContainer);
        });
    }

    sendSelect(option) {
        console.log(option);
    }
}