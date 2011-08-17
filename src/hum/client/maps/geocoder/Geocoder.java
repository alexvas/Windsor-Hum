package hum.client.maps.geocoder;

import com.google.gwt.core.client.JavaScriptObject;

public final class Geocoder extends JavaScriptObject {
    protected Geocoder() {
    }

    public native void geocode(GeocoderRequest request, GeocoderCallback callback) /*-{
        this.geocode(request, function(a, b) {
            callback.@hum.client.maps.geocoder.GeocoderCallback::back(Lcom/google/gwt/core/client/JsArray;Ljava/lang/String;)(a, b);
        });
    }-*/;


    public static native Geocoder newInstance() /*-{
        return new $wnd.google.maps.Geocoder();
    }-*/;


}
