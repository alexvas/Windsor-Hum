package hum.client;

import hum.client.adapter.JanrainWrapper;
import hum.client.events.MapsLoadedEvent;
import hum.client.events.MeEvent;
import hum.client.events.ModeEvent;
import hum.client.events.WannaSaveEvent;
import hum.client.events.WannaSaveEventHandler;
import hum.client.model.HumProxy;
import hum.client.model.UserProxy;
import hum.client.widget.Mapper;
import hum.client.widget.Mode;
import hum.client.widget.Root;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;

public class Workflow implements Runnable, WannaSaveEventHandler {

    interface HumDriver extends RequestFactoryEditorDriver<HumProxy, HumEditor> {
    }

    private HumDriver humDriver;

    @Inject
    private HumEditor humEditor;

    @Inject
    private Root root;

    @Inject
    private Mapper mapper;

    @Inject
    private JanrainWrapper janrainWrapper;

    @Inject
    private ReqFactory reqFactory;

    @Inject
    private EventBus bus;

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
        janrainWrapper.loadJanrain();
        root.init();
        RootLayoutPanel.get().add(root);
        mapper.initMap(root.getMapPlace());
        bus.fireEvent(new ModeEvent(Mode.LIST));
        bus.addHandler(WannaSaveEvent.TYPE, this);
        humDriver = GWT.create(HumDriver.class);
        humDriver.initialize(reqFactory, humEditor);
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
                bus.fireEvent(new ModeEvent(
                        user == null
                                ? Mode.LIST
                                : Mode.NEW
                ));
                ReqFactory.HumRequest request = reqFactory.humRequest();
                HumProxy hum = request.create(HumProxy.class);
                editHum(hum, request);
            }
        });
    }


    private void editHum(HumProxy hum, final ReqFactory.HumRequest request) {
        hum = request.edit(hum);
        humDriver.edit(hum, request);

        request.save(hum).with(humDriver.getPaths()).to(new Receiver<HumProxy>() {
            @Override
            public void onSuccess(HumProxy response) {
                editHum(response, request);
            }
/*
            @Override
            public void onViolation(Set<Violation> errors) {
                humDriver.setConstraintViolations(errors);
            }
*/

        });
    }


    @Override
    public void dispatch(WannaSaveEvent event) {
        RequestContext requestContext = humDriver.flush();

        if (humDriver.hasErrors()) {
            Window.alert("Errors detected locally");
            return;
        }

        requestContext.fire();
    }
}
