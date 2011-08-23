package hum.client.adapter.janrain;

import hum.client.Back;
import static hum.client.ClientUtils.CLIENT_UTILS;
import hum.client.model.HumProxy;

import com.google.inject.Singleton;

@Singleton
public class JanrainWrapper {
    public static final String JANRAIN_DOMAIN = "windsorhum.rpxnow.com";
    public static final String JANRAIN_CALLBACK = "/janrain";

    public native boolean isJanrainLoaded() /*-{
        return $wnd.RPXNOW != null;
    }-*/;

    public native void init(String appId) /*-{
        $wnd.RPXNOW.init({appId: appId, xdReceiver: '/rpx_xdcomm.html'});
    }-*/;

    public native void loadStaticModule(Back<Object> back) /*-{
        $wnd.RPXNOW.loadAndRun(['Social'], function () {
            back.@hum.client.Back::call(Ljava/lang/Object;)("done");
        });
    }-*/;
/*

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
*/

//    public native void afterLoadInitHack(String callback, String domain) /*-{
//        $wnd.RPXNOW.domain = domain;
//        $wnd.RPXNOW.token_url = callback;
//    }-*/;

    public native void show(String callback, String domain) /*-{
        $wnd.RPXNOW.show(callback, domain);
    }-*/;

    public native void publishActivity(Activity activity) /*-{
        $wnd.RPXNOW.Social.publishActivity(activity);
    }-*/;

    public native void signOut() /*-{
        $wnd.RPXNOW.Social.clearSocialCookies();
    }-*/;

/*
    public String buildGoogleShareImageSrc(HumProxy hum) {
        String src = new StringBuilder("http://maps.googleapis.com/maps/api/staticmap?center=")
                .append(hum.getPoint().getLat()).append(",").append(hum.getPoint().getLng())
                .append("&zoom=10&size=90x90&markers=color:0x")
                .append(levelHelper.color(hum.getLevel()))
                .append("%7C")
                .append(hum.getPoint().getLat()).append(",").append(hum.getPoint().getLng())
                .append("&sensor=false")
                .toString();
        return src;
    }
*/

    public String buildBingShareImageSrc(HumProxy hum) {
        String lat = CLIENT_UTILS.coordFormat.format(hum.getPoint().getLat());
        String lng = CLIENT_UTILS.coordFormat.format(hum.getPoint().getLng());
        String src = new StringBuilder("http://dev.virtualearth.net/REST/v1/Imagery/Map/Road/")
                .append(lat).append(",").append(lng)
                .append("/12?mapSize=90,90&pushpin=")
                .append(lat).append(",").append(lng)
                .append(";10;")
                .append(hum.getLevel().name().substring(0, 1))
                .append("&key=AvQDII5pngruPpQZRshSYG5tfN17m66_LhFjq1kodiYtXl0aLtls0M8syDcAoA2h")
                .toString();
        return src;
    }

}
