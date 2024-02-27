class MarkdownParser {
    constructor() {

    }

    parse(message) {
        return this.convertMarkdownToHtml(message);
    }

    convertMarkdownToHtml(markdown) {
        let html = markdown;
        html = this.convertBold(html);
        html = this.convertItalic(html);
        html = this.convertUnderline(html);
        html = this.convertCode(html);
        html = this.convertLink(html);
        html = this.convertOrderedList(html);
        html = this.convertUnorderedList(html);
        return html;
    }

    convertBold(html) {
        return html.replace(/\*\*(.*?)\*\*/g, '<b>$1</b>');
    }

    convertItalic(html) {
        return html.replace(/\*(.*?)\*/g, '<i>$1</i>');
    }

    convertUnderline(html) {
        return html.replace(/__(.*?)__/g, '<u>$1</u>');
    }

    convertCode(html) {
        return html.replace(/`(.*?)`/g, '<code>$1</code>');
    }

    convertLink(html) {
        return html.replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2">$1</a>');
    }

    convertOrderedList(html) {
        return html.replace(/(\d\..*?)(?=\n)/g, '<ol>$1</ol>');
    }

    convertUnorderedList(html) {
        return html.replace(/(\*.*?)(?=\n)/g, '<ul>$1</ul>');
    }


}