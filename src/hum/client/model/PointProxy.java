package hum.client.model;

import hum.server.model.Hum;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = Hum.Point.class)
public interface PointProxy extends ValueProxy {
    double getLat();

    void setLat(double lat);

    double getLng();

    void setLng(double lng);
}
