package model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import java.sql.SQLException;
import javax.servlet.ServletContextListener;

public class LifeCycleSetup implements ServletContextListener {

    public LifeCycleSetup() {}

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext SC = servletContextEvent.getServletContext();
        DBConnection conn = null;
        try {
            conn = new DBConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SC.setAttribute("DBConnection", conn);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext SC = servletContextEvent.getServletContext();
        DBConnection conn= (DBConnection) SC.getAttribute("DBConnection");
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SC.removeAttribute("DBConnection");
    }
}