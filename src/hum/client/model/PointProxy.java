package hum.client.model;

import hum.server.model.Hum;

import com.google.gwt.maps.client.base.LatLng;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = Hum.Point.class)
public interface PointProxy extends ValueProxy {
    double getLat();

    void setLat(double lat);

    double getLng();

    void setLng(double lng);

    static class LatLngWrapper implements PointProxy {
        private double lat;
        private double lng;

        @Override
        public double getLat() {
            return lat;
        }

        @Override
        public void setLat(double lat) {
            this.lat = lat;
        }

        @Override
        public double getLng() {
            return lng;
        }

        @Override
        public void setLng(double lng) {
            this.lng = lng;
        }

        public static PointProxy from(LatLng latLng) {
            LatLngWrapper point = new LatLngWrapper();
            point.setLat(latLng.getLatitude());
            point.setLng(latLng.getLongitude());
            return point;
        }
    }
}
