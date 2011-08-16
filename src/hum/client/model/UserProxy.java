package hum.client.model;

import hum.server.model.User;
import hum.server.services.ObjectifyLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = User.class, locator = ObjectifyLocator.class)
public interface UserProxy extends EntityProxy {
    String getDisplayName();

    String getPhoto();
}
