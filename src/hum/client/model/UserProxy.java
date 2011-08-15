package hum.client.model;

import hum.server.model.Event;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(Event.class)
public interface UserProxy extends EntityProxy {
    String getDisplayName();

    String getPhoto();
}
