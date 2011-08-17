package hum.client.model;

import hum.server.model.Hum;
import hum.server.services.ObjectifyLocator;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Hum.class, locator = ObjectifyLocator.class)
public interface HumProxy extends EntityProxy {
    enum Level {LOW, MEDIUM, HIGH}

    PointProxy getPoint();

    void setPoint(PointProxy point);

    AddressProxy getAddress();

    void setAddress(AddressProxy address);

    Level getLevel();

    void setLevel(Level level);

    Date getStart();

    void setStart(Date start);

    Date getEnd();

    void setEnd(Date end);

    String getComment();

    void setComment(String comment);
}
