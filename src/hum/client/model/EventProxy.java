package hum.client.model;

import hum.server.model.Event;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(Event.class)
public interface EventProxy extends EntityProxy {
    double getLat();

    double getLng();

    void setCountry(String country);

    void setRegion(String region);

    void setPostcode(String postcode);

    void setAddress(String address);

    void setLevel(String level);

    void setStart(Date start);

    void setEnd(Date end);

    void setComment(String comment);
}
