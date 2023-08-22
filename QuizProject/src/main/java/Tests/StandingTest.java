package Tests;

import model.Standing;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandingTest {

    @Test
    void test(){
        int UID = 13;
        int QID = 23;
        int standing = 1;
        String rev = "Name's Test, Johnny Test";
        Standing s = new Standing(UID,QID,standing,rev);
        int UID2 = s.getUID();
        int QID2 = s.getQID();
        int standing2 = s.getStanding();
        String rev2 = s.getRev();
        assertEquals(UID, UID2);
        assertEquals(QID, QID2);
        assertEquals(standing, standing2);
        assertEquals(rev, rev2);
    }

}
