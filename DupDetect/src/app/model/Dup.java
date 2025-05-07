package app.model;

import app.token.Token;

import java.util.Objects;
import java.util.TreeSet;

public class Dup implements ModelEntity, Comparable<ModelEntity> {
    private String lid;
    private String rid;

    public Dup(int id1, int id2) {
        this.lid = Integer.toString(Integer.min(id1,id2));
        this.rid = Integer.toString(Integer.max(id1,id2));
    }

    public Dup() {}

    public int getId() {
        return getLid();
    }

    public int getLid() {
        return Integer.parseInt(lid);
    }

    public int getRid() {
        return Integer.parseInt(rid);
    }

    public void setIds(int id1, int id2) {
        lid = ""+Integer.min(id1,id2);
        rid = ""+Integer.max(id1,id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lid, this.rid);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Dup other) {
            return (this.getLid() == other.getLid() && this.getRid() == other.getRid())
                    || (this.getRid() == other.getLid() && this.getLid() == other.getRid());
        }
        else return false;
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
        if (other instanceof Dup otherDup) {
            if (this.getLid() == other.getId()) {
                return Integer.compare(this.getRid(), otherDup.getRid());
            }
        }
        return Integer.compare(this.getId(), other.getId());
    }

    @Override
    public String getTitle() {
        return lid;
    }

    @Override
    public TreeSet<Token> getTokens() {
        return null;
    }

    @Override
    public void tokenize() {
        throw new RuntimeException("Tried tokenize a Dup?");
    }
}
