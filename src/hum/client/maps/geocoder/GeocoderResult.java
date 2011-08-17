package hum.client.maps.geocoder;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public final class GeocoderResult extends JavaScriptObject {
    protected GeocoderResult() {
    }

    public native GeocoderGeometry geometry() /*-{
        return this.geometry;
    }-*/;

    public native JsArray<AddressComponent> addressComponents() /*-{
        return this.address_components;
    }-*/;


}
