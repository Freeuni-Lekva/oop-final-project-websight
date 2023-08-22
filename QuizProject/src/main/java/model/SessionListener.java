package model;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;


public class SessionListener implements HttpSessionListener {

    public SessionListener() {}

    public void sessionCreated(HttpSessionEvent eve) {
        User user = new User();
        HttpSession ses = eve.getSession();
        ses.setAttribute("user", user);
    }

    public void sessionDestroyed(HttpSessionEvent eve) {
        HttpSession ses = eve.getSession();
        User user = (User) ses.getAttribute("user");
        user = new User();
        ses.removeAttribute("user");
    }
}