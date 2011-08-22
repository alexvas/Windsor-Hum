package hum.client.model;

import hum.server.model.Hum;
import hum.server.services.ObjectifyLocator;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Hum.class, locator = ObjectifyLocator.class)
public interface HumProxy extends EntityProxy {
    enum Level {
        LOW(10), MEDIUM(20), HIGH(30);
        public final int amount;

        Level(int amount) {
            this.amount = amount;
        }
    }

    PointProxy getPoint();

    void setPoint(PointProxy point);

    AddressProxy getAddress();

    void setAddress(AddressProxy address);

    Level getLevel();

    void setLevel(Level level);

    Date getStart();

    void setStart(Date start);

    String getComment();

    void setComment(String comment);
}
