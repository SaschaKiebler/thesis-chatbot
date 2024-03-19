class DocumentService {

    // Methode zum Hinzufügen eines Dokuments zur Liste in der UI
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

    // Methode zum Formatieren des Datums
    formatDate(dateString) {
        const date = new Date(dateString);
        date.setMinutes(date.getMinutes() - date.getTimezoneOffset());
        return date.toJSON().slice(0,10);
    }

    // Methode zum Löschen eines Dokuments aus der Datenbank und der UI mit der ID
    async deleteFile(fileId) {
        const response = await fetch('/api/files/' + fileId, {
            method: 'DELETE'
        });
        console.log(response);
        document.getElementById(fileId).remove();
    }

    // Methode zum Abrufen der Dokumente aus der Datenbank und Anzeigen in der UI
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

    // Methode zum Hinzufügen eines Dokuments, Ladesymbol fehlt noch da upload länger gehen kann
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