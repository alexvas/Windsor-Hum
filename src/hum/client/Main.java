package hum.client;

import hum.client.widget.Mapper;
import hum.client.widget.Root;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;

public class Main implements Runnable {

    @Inject
    private Root root;

    @Inject
    private Mapper mapper;


    @Override
    public void run() {
        AjaxLoader.AjaxLoaderOptions options = AjaxLoader.AjaxLoaderOptions.newInstance();
        options.setNoCss(true);
        options.setOtherParms("sensor=false");

        AjaxLoader.loadApi("maps", "3", new Runnable() {
            @Override
            public void run() {
                mapsLoaded();
            }
        }, options);
        RootLayoutPanel rp = RootLayoutPanel.get();
        root.init();
        rp.add(root);
        loadJanrain();
        scheduleJanrainInit();
    }

    private void scheduleJanrainInit() {
        Scheduler.get().scheduleFixedPeriod(
                new Scheduler.RepeatingCommand() {
                    @Override
                    public boolean execute() {
                        if (isJanrainLoaded()) {
                            Log.debug("janrain is loaded");
                            configureJanrain(true, "en", "ldpdafgpolkpoliepgbe");
                            return false;
                        }
                        Log.debug("waiting for janrain");
                        return true;
                    }
                },
                200
        );
    }

    private void mapsLoaded() {
        Log.debug("maps v3 loaded");
        mapper.initMap(root.getMapPlace());
    }

    private native boolean isJanrainLoaded() /*-{
        return $wnd.RPXNOW != null;
    }-*/;

    private native void configureJanrain(boolean overlay, String lang, String appId) /*-{
        $wnd.RPXNOW.overlay = overlay;
        $wnd.RPXNOW.language_preference = lang;
//        $wnd.RPXNOW.init({appId: appId, xdReceiver: '/rpx_xdcomm.html'});
    }-*/;

    private void loadJanrain() {
        StringBuilder rpxJsHost = "https:".equals(Window.Location.getProtocol())
                ? new StringBuilder("https://")
                : new StringBuilder("http://static.");
        rpxJsHost.append("rpxnow.com/js/lib/rpx.js");
        ScriptElement script = Document.get().createScriptElement();
        script.setSrc(rpxJsHost.toString());
        script.setType("text/javascript");
        Document.get().getBody().appendChild(script);
    }

}
