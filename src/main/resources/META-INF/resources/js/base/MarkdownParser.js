class MarkdownParser {
    constructor() {

    }

    parse(message) {
        return this.convertMarkdownToHtml(message);
    }

    // ruft alle Methoden auf, die Markdown in HTML umwandeln
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
        return html;
    }

    // wandelt **bold** in <b>bold</b> um
    convertBold(html) {
        return html.replace(/\*\*(.*?)\*\*/g, '<b>$1</b>');
    }

    // wandelt *italic* in <i>italic</i> um
    convertItalic(html) {
        return html.replace(/\*(.*?)\*/g, '<i>$1</i>');
    }

    // wandelt __underline__ in <u>underline</u> um
    convertUnderline(html) {
        return html.replace(/__(.*?)__/g, '<u>$1</u>');
    }

    // wandelt ```code``` in <div class="code"><pre><code>code</code></pre></div> um
    convertCode(html) {
        return html.replace(/`{3}(.*)\n([\s\S]*)\n`{3}/gim, '<div class="code"><pre><code class="$1">$2</code></pre></div>');
    }

    // wandelt `code` in <code>code</code> um
    convertInlineCode(html) {
        return html.replace(/`(.[^`]*)`/g, '<code>$1</code>');
    }

    // wandelt [link](url) in <a href="url">link</a> um
    convertLink(html) {
        return html.replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2">$1</a>');
    }

    // wandelt 1. in <ol>1.</ol> um
    convertOrderedList(html) {
        return html.replace(/(\d\..*?)(?=\n)/g, '<ol>$1</ol>');
    }

    // wandelt * in <ul><li>*</li></ul> um
    convertUnorderedList(html) {
        html = html.replace(/(\*.*?)(?=\n)/g, '<ul>$1</ul>');
        return html.replace(/(<ul>.*?<\/ul>)/g, '<li>$1</li>');
    }


}