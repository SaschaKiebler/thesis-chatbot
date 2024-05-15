
// class to convert markdown to html
class MarkdownParser {
    constructor() {

    }

    // converts markdown to html
    parse(message) {
        return this.convertMarkdownToHtml(message);
    }

    // calls all convert functions
    convertMarkdownToHtml(markdown) {
        let html = markdown;
        html = this.convertBold(html);
        html = this.convertItalic(html);
        html = this.convertUnderline(html);
        html = this.convertCode(html);
        html = this.convertInlineCode(html);
        html = this.convertLink(html);
        html = this.convertOrderedList(html);
        html = this.convertUnorderedList(html);
        html = this.convertUnorderedList2(html);
        return html;
    }

    // changes **bold** text to <b>bold</b>
    convertBold(html) {
        return html.replace(/\*\*(.*?)\*\*/g, '<b>$1</b>');
    }

    // changes *italic* in <i>italic</i>
    convertItalic(html) {
        return html.replace(/\*(.*?)\*/g, '<i>$1</i>');
    }

    // changes __underline__ in <u>underline</u>
    convertUnderline(html) {
        return html.replace(/__(.*?)__/g, '<u>$1</u>');
    }

    // changes ```code``` in <div class="code"><pre><code>code</code></pre></div>
    convertCode(html) {
        return html.replace(/`{3}(.*)\n([\s\S]*)\n`{3}/gim, '<div class="code"><pre><code class="$1">$2</code></pre></div>');
    }

    // changes `code` in <code>code</code>
    convertInlineCode(html) {
        return html.replace(/`(.[^`]*)`/g, '<code>$1</code>');
    }

    // changes [link](url) in <a href="url">link</a>
    convertLink(html) {
        return html.replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2" target="_blank">$1</a>');
    }

    // changes 1. in <ol>1.</ol>
    convertOrderedList(html) {
        return html.replace(/(\d\..*?)(?=\n)/g, '<ol>$1</ol>');
    }

    // changes * in <ul><li>*</li></ul>
    convertUnorderedList(html) {
        html = html.replace(/(\*.*?)(?=\n)/g, '<ul>$1</ul>');
        return html.replace(/(<ul>.*?<\/ul>)/g, '<li>$1</li>');
    }

    // changes \n- in <ul><li>-</li></ul>
    convertUnorderedList2(html) {
        html = html.replace(/(\n-.*?)(?=\n)/g, '<ul>$1</ul>');
        return html.replace(/(<ul>.*?<\/ul>)/g, '<li>$1</li>');
    }


}