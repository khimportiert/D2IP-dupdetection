package app.model;

public class Dup implements ModelEntity, Comparable<ModelEntity> {
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

    @Override
    public String toString() {
        return "Dup{" +
                "lid='" + lid + '\'' +
                ", rid='" + rid + '\'' +
                '}';
    }

    public String toCSV() {
        return lid + "," + rid;
    }

    public String[] getFields() {
        return new String[] { "lid", "rid" };
    }

    @Override
    public int compareTo(ModelEntity other) {
        return Integer.compare(this.getLid(), other.getId());
    }
}
