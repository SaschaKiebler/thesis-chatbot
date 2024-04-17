
class StudentService {

    constructor(studentId) {
        this.studentId = studentId;
    }

    getStudentId() {
        return this.studentId
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
        const lectureIds = [];
        const selectedLectures = document.querySelectorAll('.selected-lecture');
        for (const selectedLecture of selectedLectures) {
            lectureIds.push(selectedLecture.id);
        }
        console.log(name);
        const body = {
            name: name,
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

    async setStudentDataInUI(studentId){
        this.studentId = studentId;
        const data = await this.getStudentData();
        console.log(data);
        document.querySelector('#user-name').value = data.name;
        this.addLecturesToUI(data.lectures);
    }

    addLecturesToUI(lectures) {
        for (const lecturesKey in lectures) {
            this.addLectureToUI(lectures[lecturesKey]);
        }
    }

    // Adds a lecture to the UI
    // TODO: Dont allow lectures to be added multiple times
    addLectureToUI(lecture) {
        const lectureDiv = document.createElement('div');
        lectureDiv.className = 'selected-lecture';
        lectureDiv.id = lecture.id;
        const removeLectureButton = document.createElement("button");
        removeLectureButton.className = 'remove-lecture';
        removeLectureButton.innerHTML = 'X';
        lectureDiv.innerHTML = lecture.name ? lecture.name : lecture.value;
        lectureDiv.appendChild(removeLectureButton);
        document.querySelector('.selected-lectures').appendChild(lectureDiv);
        this.addRemoveLectureListener();
    }


    addRemoveLectureListener() {
        const removeLectureButtons = document.querySelectorAll('.remove-lecture');
        for (const removeLectureButton of removeLectureButtons) {
            removeLectureButton.addEventListener('click', () => {
                this.deleteLectureForStudent(removeLectureButton.parentElement.id);
                removeLectureButton.parentElement.remove();
            })
        }
    }
}