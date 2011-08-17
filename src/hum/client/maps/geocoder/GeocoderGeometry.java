package hum.client.maps.geocoder;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.maps.client.base.LatLng;

public final class GeocoderGeometry extends JavaScriptObject {
    protected GeocoderGeometry() {
    }

    public native LatLng location() /*-{
        return this.location;
    }-*/;
}
