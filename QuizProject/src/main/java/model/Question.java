package model;

import java.util.*;

public class Question{
    private int questionID;
    private int questionType;
    private int quizID;
    String questionText;
    ArrayList<Answer> probableAnswers; //
    Answer correctAnswer;

    public Question(){
    }
    public Question(int questionId, String questionText, int Type, int quizId, ArrayList<Answer> probableAnswers, Answer correctAnswer){
        this.questionID = questionId;
        this.questionText = questionText;
        this.questionType = Type;
        this.quizID = quizId;
        this.probableAnswers = probableAnswers;
        this.correctAnswer = correctAnswer;
    }

    public void addCorrectAnswer(Answer answer){
        this.correctAnswer = answer;
    }

    public String showQuestionText() {return "<h5>" + questionText + "</h5>";}

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType){
        this.questionType = questionType;
    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
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
        return questionID == question.questionID && questionType == question.questionType && quizID == question.quizID && Objects.equals(questionText, question.questionText) && Objects.equals(probableAnswers, question.probableAnswers) && Objects.equals(correctAnswer, question.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionID, questionType, quizID, questionText, probableAnswers, correctAnswer);
    }
}