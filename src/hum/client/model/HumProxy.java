package hum.client.model;

import hum.server.model.Hum;
import hum.server.services.ObjectifyLocator;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Hum.class, locator = ObjectifyLocator.class)
public interface HumProxy extends EntityProxy {
    enum Level {LOW, MEDIUM, HIGH}

    double getLat();

    double getLng();

    void setCountry(String country);

    void setRegion(String region);

    void setPostcode(String postcode);

    void setAddress(String address);

    void setLevel(Level level);

    void setStart(Date start);

    void setEnd(Date end);

    void setComment(String comment);
}
