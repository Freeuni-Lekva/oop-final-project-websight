package Tests;

import model.DBConnection;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private static User user;
    private static User userFilled;

    @BeforeAll
    public static void setUp() throws Exception {
        user=new User();
        userFilled = new User(1,"user",2, null);
    }

    @Test
    public void testUser() {
        assertEquals(-1,user.getUserID());
        //setId
        user.setUserID(2);
        assertEquals(2,user.getUserID());
        //type
        assertEquals(1,user.getUserType());
        //Admin
        assertEquals(true,user.isAdmin());

    }

    @Test
    public void testFilledUser() {
        assertEquals(1,userFilled.getUserID());
        assertEquals("user", userFilled.getUserName());
        try {
            userFilled.setUserName("newName");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("newName", userFilled.getUserName());
    }

}
