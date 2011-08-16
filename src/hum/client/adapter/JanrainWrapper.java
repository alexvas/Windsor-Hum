package hum.client.adapter;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.Window;
import com.google.inject.Singleton;

@Singleton
public class JanrainWrapper {
    public static final String JANRAIN_CALLBACK = "/janrain";

    public native boolean isJanrainLoaded() /*-{
        return $wnd.RPXNOW != null;
    }-*/;

    public native void configureJanrain(boolean overlay, String lang, String appId) /*-{
        $wnd.RPXNOW.overlay = overlay;
        $wnd.RPXNOW.language_preference = lang;
//        $wnd.RPXNOW.init({appId: appId, xdReceiver: '/rpx_xdcomm.html'});
    }-*/;

    public void loadJanrain() {
        StringBuilder rpxJsHost = "https:".equals(Window.Location.getProtocol())
                ? new StringBuilder("https://")
                : new StringBuilder("http://static.");
        rpxJsHost.append("rpxnow.com/js/lib/rpx.js");
        ScriptElement script = Document.get().createScriptElement();
        script.setSrc(rpxJsHost.toString());
        script.setType("text/javascript");
        Document.get().getBody().appendChild(script);
    }

    public native void show(String callback, String domain) /*-{
        $wnd.RPXNOW.show(callback, domain);
    }-*/;

    public native void signOut() /*-{
        $wnd.RPXNOW.Social.clearSocialCookies();
    }-*/;
}
