package Tests;

import model.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Date;

public class MessageTest {

    @Test
    void firstTets(){
        int type = 2;
        int to = 23;
        int from = 12;
        Message message = new Message(type,to, from);
        int newType = message.getType();
        int toID = message.getToUserID();
        int fromId = message.getFromUserID();
        assertEquals(type,newType);
        assertEquals(to,toID);
        assertEquals(from,fromId);
    }

    @Test
    void secondTest(){
        int type = 2;
        int to = 23;
        int from = 12;
        int messageID = 123;
        String subject = "Big Lebowski";
        String content = "Yo Dude let's bowl";
        Date date = new Date();
        boolean read = false;
        Message message = new Message(messageID,type,to, from, subject, content, date, read);
        int type2 = message.getType();
        int to2 = message.getToUserID();
        int from2 = message.getFromUserID();
        int messageID2 = message.getMessageID();
        String subject2 = message.getSubject();
        String content2 = message.getContent();
        Date date2 = message.getDate();
        boolean read2 = message.getReadStatus();
        assertEquals(type,type2);
        assertEquals(to,to2);
        assertEquals(from,from2);
        assertEquals(messageID,messageID2);
        assertEquals(subject,subject2);
        assertEquals(content,content2);
        assertEquals(date,date2);
        assertEquals(read,read2);
    }


}
