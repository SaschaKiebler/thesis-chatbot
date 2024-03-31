
// Service for the dokumente page
class DocumentService {

    // Method to create a list item from a file
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

    // Method to format the date
    formatDate(dateString) {
        const date = new Date(dateString);
        date.setMinutes(date.getMinutes() - date.getTimezoneOffset());
        return date.toJSON().slice(0,10);
    }

    // Method to delete a file from the database and from the UI
    async deleteFile(fileId) {
        const response = await fetch('/api/files/' + fileId, {
            method: 'DELETE'
        });
        console.log(response);
        document.getElementById(fileId).remove();
    }

    // Method to get all documents from the database and show them in the UI
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

    // TODO : Add a loading animation or a hint
    // Method to add a document to the database. (response can take a while)
    async addDocumentToDB (docName, file, lecture) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('name', docName);
        formData.append('lecture', lecture);
        const response = await fetch('/api/ingest/pdf', {
            method: 'POST',
            body: formData
        });
        console.log(response);
        document.querySelector('.doku-liste').innerHTML = '';
        await this.getDocumentsFromDBAndShowThemInUI();
    }

    // Method to create a new lecture
    async createLecture (lectureName, description) {
        console.log(lectureName, description);
        const response = await fetch('/api/lectures/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: lectureName,
                description: description
            })
        });
        console.log(response);
    }

}