package hum.client.maps.geocoder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.maps.client.base.LatLng;

public final class GeocoderRequest extends JavaScriptObject {
    protected GeocoderRequest() {
    }

    public native void location(LatLng loc) /*-{
        this.location = loc;
    }-*/;

    public native void address(String add) /*-{
        this.address = add;
    }-*/;

    public native void region(String reg) /*-{
        this.region = reg;
    }-*/;

    public static GeocoderRequest newInstance() {
        return JavaScriptObject.createObject().cast();
    }

    public void removeGwtObjectId() {
        if (!GWT.isScript()) {
            _removeGwtObjectId();
        }
    }

    public native void _removeGwtObjectId() /*-{
        delete this['__gwt_ObjectId'];
    }-*/;
}
