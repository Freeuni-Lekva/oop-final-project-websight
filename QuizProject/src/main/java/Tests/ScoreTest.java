package Tests;

import model.Score;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreTest {
    @Test
    void test(){
        int UID = 12;
        int QID = 32;
        Date date = new Date();
        int score = 22;
        Score sc = new Score(UID, QID, date, score);
        int UID2 = sc.getUID();
        int QID2 = sc.getQID();
        Date date2 = sc.getDate();
        int score2 = sc.getScore();
        assertEquals(UID, UID2);
        assertEquals(QID, QID2);
        assertEquals(date, date2);
        assertEquals(score, score2);
    }
}
