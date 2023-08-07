package model;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class User {
    public final static int ADMIN = 1;
    public final static int USER = 2;
    private int ID;
    private String name;
    private int type;
    DBConnection dbConnection;

    public User() {
        ID = -1;
        name = "";
        type = 1;
        dbConnection = null;
    }

    public User(int userID, String userName, int userType, DBConnection dbConnection) {
        this.ID = userID;
        this.name = userName;
        this.type = userType;
        this.dbConnection = dbConnection;
    }

    public void setUserID(int userID) {
        this.ID = userID;
    }

    public int getUserID() {
        return ID;
    }

    public void setUserType(int userType) {
        this.type = userType;
    }

    public int getUserType() {
        return type;
    }

    public void setUserName(String userName) throws SQLException {
        this.name = userName;
        if (dbConnection == null) return;
        dbConnection.setUserName(ID, userName);
    }

    public String getUserName() {
        return name;
    }

    public boolean isAdmin() {
        return type == ADMIN;
    }



}
