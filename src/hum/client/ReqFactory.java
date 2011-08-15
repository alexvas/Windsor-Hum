package hum.client;

import hum.client.model.EventProxy;
import hum.client.model.UserProxy;
import hum.server.guice.InjectingServiceLocator;
import hum.server.services.EventService;
import hum.server.services.UserService;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;

public interface ReqFactory extends RequestFactory {
    EventRequest eventRequest();

    UserRequest userRequest();

    @Service(value = EventService.class, locator = InjectingServiceLocator.class)
    interface EventRequest extends RequestContext {
        List<EventProxy> my();

        List<EventProxy> all();

        EventProxy latest();

        InstanceRequest<EventProxy, Void> save();
    }

    @Service(value = UserService.class, locator = InjectingServiceLocator.class)
    interface UserRequest extends RequestContext {
        UserProxy me();
    }
}