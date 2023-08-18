package model;

public class Standing {
    private int UID;
    private int QID;
    private int standing;
    private String rev;

    public Standing(int UID, int QID, int standing, String rev) {
        this.UID = UID;
        this.QID = QID;
        this.standing = standing;
        this.rev = rev;
    }

    public int getUID() {return UID;}

    public int getQID() {return QID;}

    public int getStanding() {return standing;}

    public String getRev() {return rev;}
}
