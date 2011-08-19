package hum.client;

import hum.client.events.ModeEvent;
import hum.client.model.AddressProxy;
import hum.client.model.HumProxy;
import hum.client.model.PointProxy;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;

@Singleton
public class HumWorkflow {
    @Inject
    private ReqFactory factory;

    @Inject
    private EventBus bus;

    @Inject
    private HumEditor humEditor;

    interface HumDriver extends RequestFactoryEditorDriver<HumProxy, HumEditor> {
    }

    private HumDriver humDriver;

    private ReqFactory.HumRequest humRequest;

    public void init() {
        humDriver = GWT.create(HumDriver.class);
        humDriver.initialize(factory, humEditor);
        humRequest = factory.humRequest();
    }

    void edit(HumProxy in) {
        HumProxy owned = humRequest.edit(in);
        humDriver.edit(owned, humRequest);

        humRequest.save(owned).with(humDriver.getPaths()).to(new Receiver<HumProxy>() {
            @Override
            public void onSuccess(HumProxy response) {
                humRequest = factory.humRequest();
                edit(response);
                bus.fireEvent(new ModeEvent(Mode.LAST));
            }

            @Override
            public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                humDriver.setConstraintViolations(violations);
            }
        });
    }

    public HumProxy createHum() {
        return humRequest.create(HumProxy.class);
    }

    public PointProxy createPoint() {
        return humRequest.create(PointProxy.class);
    }

    public AddressProxy createAddress() {
        return humRequest.create(AddressProxy.class);
    }

    public void save() {
        RequestContext requestContext = humDriver.flush();

        if (humDriver.hasErrors()) {
            Window.alert("Errors detected locally");
            return;
        }

        requestContext.fire();
    }
}
