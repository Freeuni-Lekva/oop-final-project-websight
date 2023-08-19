package model.Question;

import model.Answer;
import model.DBConnection;

import java.sql.*;
import java.util.*;

public class Question{
    private int questionId;
    private int questionType;
    private int quizId;
    String questionText;
    ArrayList<Answer> probableAnswers; //
    Answer correctAnswer;

    public Question(){
    }
    public Question(int questionId, String questionText, int Type, int quizId, ArrayList<Answer> probableAnswers, Answer correctAnswer){
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionType = Type;
        this.quizId = quizId;
        this.probableAnswers = probableAnswers;
        this.correctAnswer = correctAnswer;
    }

    public void addCorrectAnswer(Answer answer){
        this.correctAnswer = answer;
    }

    public String showQuestionText() {return "<h5>" + questionText + "</h5>";}

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public ArrayList<Answer> getProbableAnswers() {
        return probableAnswers;
    }

    public void setProbableAnswers(ArrayList<Answer> probableAnswers) {
        this.probableAnswers = probableAnswers;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return questionId == question.questionId && questionType == question.questionType && quizId == question.quizId && Objects.equals(questionText, question.questionText) && Objects.equals(probableAnswers, question.probableAnswers) && Objects.equals(correctAnswer, question.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, questionType, quizId, questionText, probableAnswers, correctAnswer);
    }
}