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

    public void setLid(int id) {
        lid = ""+id;
    }

    public void setRid(int id) {
        lid = ""+id;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Dup other) {
            return (this.getLid() == other.getLid() && this.getRid() == other.getRid())
                    || (this.getRid() == other.getLid() && this.getLid() == other.getRid());
        }
        else return false;
    }
}
