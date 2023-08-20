package Listeners;

import model.DBConnection;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
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
        ServletContext context = servletContextEvent.getServletContext();
        DBConnection dbConnection = (DBConnection) context.getAttribute("DBConnection");
        try {
            dbConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        context.removeAttribute("DBConnection");
    }
}
