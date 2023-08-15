package Listeners;

import model.DBConnection;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        DBConnection dbConnection = null;
        try {
            dbConnection = new DBConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute("DBConnection", dbConnection);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
