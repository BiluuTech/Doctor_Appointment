package app.biluutech.medicineprescription;

public class DoctorModel {

    String dID,image,name, age, time, date, email, address, gender, specialization, DoctorStatus, phone;

    public DoctorModel() {
    }

    public DoctorModel(String dID, String image, String name, String age, String time, String date, String email, String address, String gender, String specialization, String doctorStatus, String phone) {
        this.dID = dID;
        this.image = image;
        this.name = name;
        this.age = age;
        this.time = time;
        this.date = date;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.specialization = specialization;
        DoctorStatus = doctorStatus;
        this.phone = phone;
    }

    public String getdID() {
        return dID;
    }

    public void setdID(String dID) {
        this.dID = dID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDoctorStatus() {
        return DoctorStatus;
    }

    public void setDoctorStatus(String doctorStatus) {
        DoctorStatus = doctorStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
