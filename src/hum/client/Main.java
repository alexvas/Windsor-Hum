package hum.client;

import hum.client.widget.Mapper;
import hum.client.widget.Root;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.ajaxloader.client.AjaxLoader;
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
        configureJanrain(true, "en");
        loadJanrain();
        RootLayoutPanel rp = RootLayoutPanel.get();
        root.init();
        rp.add(root);
    }

    private void mapsLoaded() {
        Log.debug("maps v3 loaded");
        mapper.initMap(root.getMapPlace());
    }

    private native void configureJanrain(boolean overlay, String lang) /*-{
        $wnd.RPXNOW.overlay = overlay;
        $wnd.RPXNOW.language_preference = lang;
    }-*/;

    private void loadJanrain() {
        StringBuilder rpxJsHost = "https:".equals(Window.Location.getProtocol())
                ? new StringBuilder("https://")
                : new StringBuilder("http://static.");
        rpxJsHost.append("rpxnow.com/js/lib/rpx.js");
        ScriptElement script = Document.get().createScriptElement(rpxJsHost.toString());
        script.setType("text/javascript");
        Document.get().appendChild(script);
    }

}
