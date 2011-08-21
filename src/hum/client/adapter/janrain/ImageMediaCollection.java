package hum.client.adapter.janrain;

import com.google.gwt.core.client.JavaScriptObject;

public final class ImageMediaCollection extends JavaScriptObject {
    protected ImageMediaCollection() {
    }

    public native void addImage(String src, String href) /*-{
        this.addImage(src, href);
    }-*/;

    public static native ImageMediaCollection getInstance() /*-{
        return new $wnd.RPXNOW.Social.ImageMediaCollection()
    }-*/;
}
