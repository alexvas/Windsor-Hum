package hum.client.maps.geocoder;

import com.google.gwt.core.client.JsArray;

public abstract class GeocoderCallback {
    public abstract void back(JsArray<GeocoderResult> results, String status);
}
