package hum.client;

import hum.client.adapter.addthis.AddthisWrapper;
import hum.client.adapter.janrain.JanrainWrapper;
import hum.client.events.MapsLoadedEvent;
import hum.client.events.MeEvent;
import hum.client.events.ModeEvent;
import hum.client.model.UserProxy;
import hum.client.widget.Mapper;
import hum.client.widget.Root;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class Workflow implements Runnable {
    @Inject
    private Root root;

    @Inject
    private Mapper mapper;

    @Inject
    private JanrainWrapper janrainWrapper;

    @Inject
    private AddthisWrapper addthisWrapper;

    @Inject
    private ReqFactory reqFactory;

    @Inject
    private EventBus bus;

    @Inject
    private HumWorkflow humWorkflow;

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
        whoAmI();
//        janrainWrapper.loadJanrain();
        root.init();
        RootLayoutPanel.get().add(root);
        mapper.initMap(root.getMapPlace());
        bus.fireEvent(new ModeEvent());
        humWorkflow.init();
        addthisWrapper.load("ra-4e50705441f5a8f1");
    }

    private void mapsLoaded() {
        Log.debug("maps v3 loaded");
        bus.fireEvent(new MapsLoadedEvent());
    }

    private void whoAmI() {
        reqFactory.userRequest().me().with("info").fire(new Receiver<UserProxy>() {
            @Override
            public void onSuccess(UserProxy user) {
                bus.fireEvent(new MeEvent(user));
                if (user == null) {
                    humWorkflow.showMeLastSubmitted();
                } else {
                    humWorkflow.reportNewHum();
                }
            }
        });
    }

}
