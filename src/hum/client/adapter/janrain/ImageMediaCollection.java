package hum.client.adapter.janrain;

import com.google.gwt.core.client.JavaScriptObject;

public final class ImageMediaCollection extends JavaScriptObject {
    protected ImageMediaCollection() {
    }

    public native void buildMarkerImage(double lat, double lng, String color, String href) /*-{
        var src = 'http://maps.googleapis.com/maps/api/staticmap?center=' + lat + ',' + lng +
                '&zoom=10&size=200x200&markers=color:0x' + color + '%7C' + lat + ',' + lng + '&sensor=false" />';
        this.addImage(src, href);
    }-*/;

    public native void addImage(String src, String href) /*-{
        this.addImage(src, href);
    }-*/;

    public static native ImageMediaCollection getInstance() /*-{
        return new $wnd.RPXNOW.Social.ImageMediaCollection()
    }-*/;
}
