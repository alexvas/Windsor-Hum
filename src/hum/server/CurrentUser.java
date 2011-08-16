package hum.server;

import hum.server.model.User;

import java.io.Serializable;

import com.google.inject.servlet.SessionScoped;
import com.googlecode.objectify.Key;

@SessionScoped
public class CurrentUser implements Serializable {
    private static final long serialVersionUID = 44939070302239L;

    private Key<User> userKey;

    public Key<User> getUserKey() {
        return userKey;
    }

    public void setUserId(Long id) {
        userKey = new Key<User>(User.class, id);
    }
}
