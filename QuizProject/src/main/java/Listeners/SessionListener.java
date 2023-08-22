package Listeners;

import model.User;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    public SessionListener() {}

    @Override
    public void sessionCreated(HttpSessionEvent eve) {
        User user = new User();
        HttpSession ses = eve.getSession();
        ses.setAttribute("user", user);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent eve) {
        HttpSession ses = eve.getSession();
        User user = (User) ses.getAttribute("user");
        user = new User();
        ses.removeAttribute("user");
    }
}
