package hum.client.maps;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.maps.client.base.LatLng;

public final class MapOptions extends JavaScriptObject {
    protected MapOptions() {
    }

    public native void center(LatLng c) /*-{
        this.center = c;
    }-*/;

    public native void zoom(int z) /*-{
        this.zoom = z;
    }-*/;

    public native void roadmap() /*-{
        this.mapTypeId = $wnd.google.maps.MapTypeId.ROADMAP;
    }-*/;

    public static MapOptions newInstance() {
        return JavaScriptObject.createObject().cast();
    }
}
