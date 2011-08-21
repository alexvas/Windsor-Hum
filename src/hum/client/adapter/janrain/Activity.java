package hum.client.adapter.janrain;

import com.google.gwt.core.client.JavaScriptObject;

public final class Activity extends JavaScriptObject {
    protected Activity() {
    }

    public native void setTitle(String title) /*-{
        this.setTitle(title);
    }-*/;

    public native void setDescription(String description) /*-{
        this.setDescription(description);
    }-*/;

    public native void setUserGeneratedContent(String content) /*-{
        this.setUserGeneratedContent(content);
    }-*/;

    public native void setMediaItem(ImageMediaCollection images) /*-{
        this.setMediaItem(images);
    }-*/;

    public static native Activity getInstance(String shareDisplay, String action, String url) /*-{
        return new $wnd.RPXNOW.Social.Activity(shareDisplay, action, url)
    }-*/;
}
