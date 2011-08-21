package hum.client;

import hum.client.adapter.janrain.Activity;
import hum.client.adapter.janrain.ImageMediaCollection;
import hum.client.adapter.janrain.JanrainWrapper;
import hum.client.events.ErrorEvent;
import hum.client.events.GonnaShareEvent;
import hum.client.events.GonnaShareEventHandler;
import hum.client.events.LevelEvent;
import hum.client.events.OverviewEvent;
import hum.client.events.StartedEvent;
import hum.client.model.AddressProxy;
import hum.client.model.HumProxy;
import hum.client.model.PointProxy;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.impl.AbstractEditorDelegate;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;

@Singleton
public class HumWorkflow implements GonnaShareEventHandler {
    @Inject
    private ReqFactory factory;

    @Inject
    private EventBus bus;

    @Inject
    private HumEditor editor;

    @Inject
    private Validator validator;

    @Inject
    private ModeHolder modeHolder;

    @Inject
    private LevelHelper levelHelper;

    @Inject
    private JanrainWrapper janrainWrapper;

    interface HumDriver extends RequestFactoryEditorDriver<HumProxy, HumEditor> {
    }

    private HumDriver driver;

    private ReqFactory.HumRequest editingRequest;

    public void init() {
        driver = GWT.create(HumDriver.class);
        driver.initialize(factory, editor);
        editingRequest = factory.humRequest();
        bus.addHandler(GonnaShareEvent.TYPE, this);
    }

    public void reportNewHum() {
        edit(createHum());
        modeHolder.newHumCreated();
        bus.fireEvent(new LevelEvent(null));
        bus.fireEvent(new StartedEvent(null));
    }

    private HumReceiver saveReceiver = new HumReceiver();

    private HumReceiver latestReceiver = new HumReceiver() {
        @Override
        public void onSuccess(HumProxy response) {
            if (response == null) {
                reportNewHum();
            } else {
                super.onSuccess(response);
                bus.fireEvent(new LevelEvent(response.getLevel()));
                bus.fireEvent(new StartedEvent(response.getStart()));
            }
        }
    };

    public void editLastHum() {
        factory.humRequest().latest().with(driver.getPaths()).to(latestReceiver).fire();
    }

    public void overviewLastSubmitted() {
        factory.humRequest().overview().with(driver.getPaths()).to(
                new Receiver<List<HumProxy>>() {
                    @Override
                    public void onSuccess(List<HumProxy> response) {
                        bus.fireEvent(new OverviewEvent(response));
                    }
                }
        ).fire();
    }

    private void edit(HumProxy in) {
        HumProxy owned = editingRequest.edit(in);
        driver.edit(owned, editingRequest);
        editingRequest.save(owned).with(driver.getPaths()).to(saveReceiver);
    }

    private HumProxy createHum() {
        return editingRequest.create(HumProxy.class);
    }

    public PointProxy createPoint() {
        return editingRequest.create(PointProxy.class);
    }

    public AddressProxy createAddress() {
        return editingRequest.create(AddressProxy.class);
    }

    public void save() {
        RequestContext requestContext = driver.flush();

        HumProxy hum = ((AbstractEditorDelegate<HumProxy, HumEditor>) editor.getDelegate()).getObject();


        List<ConstraintViolation<HumProxy>> violations = validator.validate(hum);
        if (!violations.isEmpty()) {
            bus.fireEvent(new ErrorEvent(violations));
            return;
        }

        if (driver.hasErrors()) {
            // these errors escaped
            Window.alert("Errors detected locally");
            return;
        }

        requestContext.fire();
    }

    private class HumReceiver extends Receiver<HumProxy> {
        @Override
        public void onSuccess(HumProxy response) {
            editingRequest = factory.humRequest();
            edit(response);
            modeHolder.lastHumLoaded();
        }

        @Override
        public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
            driver.setConstraintViolations(violations);
        }
    }

    @Override
    public void dispatch(GonnaShareEvent event) {
        final Activity activity = Activity.getInstance(
                "Share The Hum",
                "I've heard a hum again",
                Window.Location.getHref()
        );
        HumProxy hum = ((AbstractEditorDelegate<HumProxy, HumEditor>) editor.getDelegate()).getObject();
        ImageMediaCollection images = ImageMediaCollection.getInstance();
        images.buildMarkerImage(
                hum.getPoint().getLat(),
                hum.getPoint().getLng(),
                levelHelper.color(hum.getLevel()),
                Window.Location.getHref()
        );
        activity.setMediaItem(images);
        String comment = hum.getComment() == null
                ? "I've heard it again!"
                : hum.getComment();
        activity.setUserGeneratedContent(comment);
        janrainWrapper.init("ldpdafgpolkpoliepgbe");
        janrainWrapper.loadStaticModule(new Back<Object>() {
            @Override
            public void call(Object o) {
                janrainWrapper.publishActivity(activity);
            }
        });
    }
}
