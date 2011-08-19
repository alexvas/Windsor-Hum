package hum.server;

import hum.server.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.googlecode.objectify.Key;

@Singleton
public class CurrentUser {
    @Inject
    Provider<HttpServletRequest> req;

    public Key<User> getUserKey() {
        Long id = (Long) session().getAttribute("currentUser");

        return id == null
                ? null
                : new Key<User>(User.class, id);
    }

    public void setUserId(Long id) {
        session().setAttribute("currentUser", id);
    }

    public void removeKey() {
        session().removeAttribute("currentUser");
    }

    private HttpSession session() {
        return req.get().getSession();
    }
}
