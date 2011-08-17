package hum.client.maps.geocoder;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public final class AddressComponent extends JavaScriptObject {
    protected AddressComponent() {
    }

    public native String shortName() /*-{
        return this.short_name;
    }-*/;

    public native JsArrayString types() /*-{
        return this.types;
    }-*/;
}
