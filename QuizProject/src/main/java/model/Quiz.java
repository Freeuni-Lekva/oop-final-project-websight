package model;

import java.util.*;

public class Quiz {
    private static final int QUESTION_RESPONSE = 1;
    private static final int FILL_BLANK = 2;
    private static final int MULTIPLE_CHOICE = 3;
    private static final int PICTURE_RESPONSE = 4;
    private  int quizId;
    private int authorId;
    private String title;
    private String description;
    private Date creationDate;

    private boolean isRandomOrderQuestions;
    private boolean isOnePage;
    private boolean isImmediateCorrection;
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;
    public Quiz() {
    }

    public Quiz(int quizId, int creatorId, String title, String description, Date creationDate, boolean isRandom, boolean isOnePage, boolean isImmediateCorrection, ArrayList<Question> questions) {
        this.quizId = quizId;
        this.authorId = creatorId;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.isRandomOrderQuestions = isRandom;
        this.isOnePage = isOnePage;
        this.isImmediateCorrection = isImmediateCorrection;
        this.questions = questions;
    }

    public ArrayList<Question> getQuestions(){
        ArrayList<Question> result = new ArrayList<Question>();
        Set<Integer> usedIndex = new HashSet<Integer>();
        if (isRandomOrderQuestions){
            while (result.size() < questions.size()){
                Random rand = new Random();
                int randomIndex = rand.nextInt(questions.size());
                if(!usedIndex.contains(randomIndex)){
                    usedIndex.add(randomIndex);
                    result.add(questions.get(randomIndex));

                }
            }
        } else {
            for (int i = 0; i < questions.size(); i++){
                result.add(questions.get(i));
            }
        }
        return result;
    }

    public int getQuizID() {
        return quizId;
    }

    public int getAuthorID() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isRandomOrderQuestions() {
        return isRandomOrderQuestions;
    }

    public boolean isOnePage() {
        return isOnePage;
    }

    public boolean isImmediateCorrection() {
        return isImmediateCorrection;
    }

    public void addQuestionToQuizz(Question question){
        questions.add(question);
    }



    public void setQuizID(int quizId) {
        this.quizId = quizId;
    }

    public void setAuthorID(int authorId) {
        this.authorId = authorId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setRandomOrderQuestions(boolean randomOrderQuestions) {
        isRandomOrderQuestions = randomOrderQuestions;
    }

    public void setOnePage(boolean onePage) {
        isOnePage = onePage;
    }

    public void setImmediateCorrection(boolean immediateCorrection) {
        isImmediateCorrection = immediateCorrection;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}

