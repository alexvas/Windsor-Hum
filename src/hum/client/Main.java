package hum.client;

import hum.client.adapter.JanrainWrapper;
import hum.client.widget.Mapper;
import hum.client.widget.Root;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;

public class Main implements Runnable {

    @Inject
    private Root root;

    @Inject
    private Mapper mapper;

    @Inject
    private JanrainWrapper janrainWrapper;

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
        janrainWrapper.loadJanrain();
    }

    private void mapsLoaded() {
        Log.debug("maps v3 loaded");
        mapper.initMap(root.getMapPlace());
    }

}
