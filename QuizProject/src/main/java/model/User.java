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
    DBConnection conn;

    public User() {
        ID = -1;
        name = "";
        type = 1;
        conn = null;
    }

    public User(int userID, String userName, DBConnection dbConnection) {
        try {
            this.ID = userID;
            this.name = userName;
            this.conn = dbConnection;
            if ( dbConnection.getAdminUserIDsSet().contains(userID) ) this.type = ADMIN;
            else this.type = USER;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public User(int userID, String userName, int userType, DBConnection dbConnection) {
        this.ID = userID;
        this.name = userName;
        this.type = userType;
        this.conn = dbConnection;
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
        if (conn == null) return;
        conn.setUserName(ID, userName);
    }

    public String getUserName() {
        return name;
    }

    public boolean isAdmin() {
        return type == ADMIN;
    }

    public boolean setPassword(String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (conn == null) return false;
        String passwordHash;
        passwordHash = HashingPassword.createHash(password);
        conn.setPassword(ID, passwordHash);
        return true;
    }

    public boolean checkPassword(String password) throws SQLException {
        String correctHash = conn.getPassword(ID);
        try {
            return HashingPassword.PasswordValidation(password, correctHash);
        } catch (NoSuchAlgorithmException e) {
            return false;
        } catch (InvalidKeySpecException e) {
            return false;
        }
    }


}
