package hum.client.model;

import hum.server.model.User;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(User.class)
public interface UserProxy extends EntityProxy {
    String getDisplayName();

    String getPhoto();
}
