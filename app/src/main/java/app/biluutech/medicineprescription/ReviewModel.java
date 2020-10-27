package app.biluutech.medicineprescription;

public class ReviewModel {

    private  String patientName, comment;

    public ReviewModel() {
    }

    public ReviewModel(String patientName, String comment) {
        this.patientName = patientName;
        this.comment = comment;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
