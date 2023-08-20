package model;

public class CreationQuiz {
    private int quizID;
    private int userID;
    private java.util.Date createDate;
    private String quizName;

    public CreationQuiz(int quizID, int userID, java.util.Date createDate, String quizName) {
        this.quizID = quizID;
        this.userID = userID;
        this.createDate = createDate;
        this.quizName = quizName;
    }

    public int getQuizID() {
        return quizID;
    }
    public int getUserID() {
        return userID;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public String getQuizName() {
        return quizName;
    }
}
