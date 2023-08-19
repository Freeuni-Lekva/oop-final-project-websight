package Tests;

import junit.framework.TestCase;
import model.Answer;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

public class AnswerTest extends TestCase {
    private static Answer answer;
    private static Answer answerFilled;

    @BeforeAll
    public  void setUp(){
        answer       = new Answer();
        answerFilled = new Answer(1,"Sample text");
    }

    @Test
    public void constructorTest(){
        Answer answer1 = new Answer(1, "Sample text");
        assertEquals(1, answer1.getAnswerId());
        assertEquals("Sample text", answer1.getText());
    }

    @Test
    public void testGetters() {
        assertEquals(-1, answer.getAnswerId());
        assertEquals("", answer.getText());
        assertEquals(1, answerFilled.getAnswerId());
        assertEquals("Sample text", answerFilled.getText());
    }

    @Test
    public void testSetters() {
        answer.setAnswerId(2);
        answer.setText("New text");
        assertEquals(2, answer.getAnswerId());
        assertEquals("New text", answer.getText());
    }

    @Test
    public void testEquals() {
        Answer answer1 = new Answer(1, "Text");
        Answer answer2 = new Answer(1, "Text");
        Answer answer3 = new Answer(2, "Different text");

        assertTrue(answer1.equals(answer2));
        assertFalse(answer1.equals(answer3));
    }


}