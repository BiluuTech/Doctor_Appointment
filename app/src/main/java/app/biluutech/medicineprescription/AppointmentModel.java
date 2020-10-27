package app.biluutech.medicineprescription;

public class AppointmentModel {

    private String name, age, gender, time, date, image, pid;

    public AppointmentModel() {
    }

    public AppointmentModel(String name, String age, String gender, String time, String date, String image, String pid) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.time = time;
        this.date = date;
        this.image = image;
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
