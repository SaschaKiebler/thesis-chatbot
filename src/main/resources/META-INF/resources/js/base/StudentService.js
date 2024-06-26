
class StudentService {

    constructor(studentId) {
        this.studentId = studentId;
        this.lectures = [];
        this.name = '';
    }

    getStudentId() {
        return this.studentId;
    }

    getLectures() {
        return this.lectures;
    }

    getName() {
        return this.name;
    }

    async fetchNewStudentId() {
        await fetch(
            '/api/student/id',
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        ).then(
            response => response.json()
        )
        .then(
            data => {
                this.studentId = data.studentId;
                localStorage.setItem('personal-id', this.studentId);
                this.name = "";
                data.name = "";
                this.lectures = data.lectures;
                return data
            }
        )
        .catch(
            error => console.log(error)
        )
    }

    async getStudentData() {
        return await fetch(
            '/api/student/data/' + this.studentId,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        ).then(
            response => response.json()
        )
        .then(
            data => {
                this.name = data.name ? data.name : "";

                this.lectures = data.lectures;
                return data
            }
        )
        .catch(
            error => console.log(error)
        )
    }

    async deleteLectureForStudent(lectureId) {
        await fetch(
            '/api/student/lecture/' + this.studentId + '/' + lectureId,
            {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        ) .catch(
            error => console.log(error)
        )
    }

    async updateStudentData() {
        const name = document.querySelector('#user-name').value;
        const sanitizedName = this.sanitizeString(name);
        const lectureIds = [];
        const selectedLectures = document.querySelectorAll('.selected-lecture');
        for (const selectedLecture of selectedLectures) {
            if (lectureIds.includes(selectedLecture.id)) {
                continue;
            }
            lectureIds.push(selectedLecture.id);
        }

        this.name = sanitizedName;
        localStorage.setItem('name', this.name);
        const body = {
            name: sanitizedName,
            lectures: lectureIds
        }
        await fetch(
            '/api/student/data/' + this.studentId,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            }
        ).catch(
            error => console.log(error)
        )
    }

    async getStudentScores(studentId) {
        if (!studentId) {
            return;
        }
        return await fetch("/api/quiz/results/" + studentId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            response => response.json()
        )
        .then(
            data => {
                return data
            }
        )
    }

    async setStudentDataInUI(studentId){
        this.studentId = studentId;
        const data = await this.getStudentData();
        document.querySelector('#user-name').value = data.name;
        this.removeAllLecturesFromUI();
        this.addLecturesToUI(data.lectures);
    }

    addLecturesToUI(lectures) {
        for (const lecturesKey in lectures) {
            this.addLectureToUI(lectures[lecturesKey]);
        }
    }

    // Adds a lecture to the UI as a button with a remove button
    // TODO: Dont allow lectures to be added multiple times
    addLectureToUI(lecture) {
        const lectureDiv = document.createElement('div');
        lectureDiv.className = 'selected-lecture';
        lectureDiv.id = lecture.id;

        const removeLectureButton = document.createElement('button');
        removeLectureButton.className = 'remove-lecture';
        removeLectureButton.innerHTML = 'X';

        lectureDiv.innerHTML = lecture.name ? lecture.name : lecture.value;
        lectureDiv.appendChild(removeLectureButton);
        document.querySelector('.selected-lectures').appendChild(lectureDiv);

        // Directly add event listener to the new remove button
        removeLectureButton.addEventListener('click', async () => {
            await this.handleRemoveLecture(removeLectureButton);
        });
    }

    addRemoveLectureListener() {
        const removeLectureButtons = document.querySelectorAll('.remove-lecture');

        // Ensure each button has the event listener attached
        for (const removeLectureButton of removeLectureButtons) {
            // Remove existing event listeners to avoid multiple bindings
            const newButton = removeLectureButton.cloneNode(true);
            removeLectureButton.replaceWith(newButton);

            newButton.addEventListener('click', async () => {
                await this.handleRemoveLecture(newButton);
            });
        }
    }

    async handleRemoveLecture(removeLectureButton) {
        await this.deleteLectureForStudent(removeLectureButton.parentElement.id);
        removeLectureButton.parentElement.remove();
    }

    removeAllLecturesFromUI() {
        const removeLectureButtons = document.querySelectorAll('.remove-lecture');
        for (const removeLectureButton of removeLectureButtons) {
            removeLectureButton.parentElement.remove();
        }
    }




 sanitizeString(str) {
        return str.replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#39;")
            .replace(/`/g, "&#96;");
    }

}