package app.biluutech.medicineprescription;

public class ItemModel {

    private String name,desc,pres;

    public ItemModel(String name, String desc, String pres) {
        this.name = name;
        this.desc = desc;
        this.pres = pres;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }
}
