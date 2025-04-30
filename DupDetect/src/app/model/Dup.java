package app.model;

public class Dup implements ModelEntity {
    private String lid;
    private String rid;

    public Dup() { }

    public int getId() {
        return getLid();
    }

    public int getLid() {
        return Integer.parseInt(lid);
    }

    public int getRid() {
        return Integer.parseInt(rid);
    }
}
