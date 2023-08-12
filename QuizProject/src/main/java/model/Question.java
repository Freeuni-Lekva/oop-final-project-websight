package model;

import java.sql.*;
import java.util.*;

public abstract class Question implements Comparable {
    final int questionId;
    final int questionType;
    final int questionNum;
    final int quizId;
    String questionText;

    public Question(int Id, String Text, int Type, int Num, int quizId, DBConnection db) throws SQLException {
        this.questionId = Id;
        this.questionText = Text;
        this.questionType = Type;
        this.questionNum = Num;
        this.quizId = quizId;
        addAnswers(db);
    }

    public abstract void addAnswers(DBConnection db) throws NumberFormatException, SQLException;

    public String showQuestionText() {return "<h5>" + questionText + "</h5>";}

    public int getQuestionId() {return questionId;}

    public String getQuestionText() {return questionText;}

    public int getQuestionType() {return questionType;}

    public int getQuestionNum() {return questionNum;}

    public int getQuizId() {return quizId;}

    @Override
    public int compareTo(Object o) {
        Question tmp = (Question)o;
        return questionNum - tmp.questionNum;
    }
}