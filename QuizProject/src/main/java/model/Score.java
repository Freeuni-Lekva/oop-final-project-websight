package model;

public class Score {
    private int UID;
    private int QID;
    private java.util.Date date;
    private int score;

    public Score(int UID, int QID, java.util.Date date, int score) {
        this.UID = UID;
        this.QID = QID;
        this.date = date;
        this.score = score;
    }

    public int getUID() {return UID;}

    public int getQID() {return QID;}

    public int getScore() {return score;}

    public java.util.Date getDate() {return date;}
}
