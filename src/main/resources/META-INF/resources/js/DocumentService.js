class DocumentService {

    createListItem(file) {
        const li = document.createElement('li');
        li.className = 'doku-liste-eintrag';
        li.id = file.id;

        const a = document.createElement('a');
        a.href = '/api/files/' + file.id;
        a.target = '_blank';
        a.innerText = file.name;
        li.appendChild(a);

        const div = document.createElement('div');
        div.innerText = this.formatDate(file.created);
        li.appendChild(div);

        const button = document.createElement('button');
        button.className = 'delete-button';
        button.innerText = 'löschen';
        button.onclick = async () => {
            await this.deleteFile(file.id);
        };
        li.appendChild(button);

        return li;
    }

    formatDate(dateString) {
        const date = new Date(dateString);
        date.setMinutes(date.getMinutes() - date.getTimezoneOffset());
        return date.toJSON().slice(0,10);
    }

    async deleteFile(fileId) {
        const response = await fetch('/api/files/' + fileId, {
            method: 'DELETE'
        });
        console.log(response);
        document.getElementById(fileId).remove();
    }

    async getDocumentsFromDBAndShowThemInUI () {
        const response = await fetch('/api/files/all', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        console.log(data);
        data.forEach((file) => {
            const li = this.createListItem(file);
            document.querySelector('.doku-liste').appendChild(li);
        });
    }

    async addDocumentToDB (docName, file) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('name', docName);
        const response = await fetch('/api/ingest/pdf', {
            method: 'POST',
            body: formData
        });
        console.log(response);
        document.querySelector('.doku-liste').innerHTML = '';
        await this.getDocumentsFromDBAndShowThemInUI();
    }

}