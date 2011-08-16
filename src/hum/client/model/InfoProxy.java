package hum.client.model;

import hum.server.model.User;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = User.Info.class)
public interface InfoProxy extends ValueProxy {
    String getDisplayName();

    String getPhoto();
}
