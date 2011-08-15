package hum.server;

import java.io.Serializable;

import com.google.inject.servlet.SessionScoped;

@SessionScoped
public class CurrentUser implements Serializable {
    private static final long serialVersionUID = 44939070302239L;

    public Long userId;
}
