package model;

import java.util.Objects;

public class Answer {
    private int answerId;
    private String Text;

    public Answer() {

    }

    public Answer(int answerId, String text) {
        this.answerId = answerId;
        Text = text;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return answerId == answer.answerId && Objects.equals(Text, answer.Text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerId, Text);
    }
}
