package hum.client;

import hum.client.model.HumProxy;
import hum.client.model.UserProxy;
import hum.server.guice.InjectingServiceLocator;
import hum.server.services.HumService;
import hum.server.services.UserService;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;

public interface ReqFactory extends RequestFactory {
    HumRequest humRequest();

    UserRequest userRequest();

    @Service(value = HumService.class, locator = InjectingServiceLocator.class)
    interface HumRequest extends RequestContext {

        Request<HumProxy> latest();

        Request<HumProxy> save(HumProxy hum);

        Request<List<HumProxy>> mine();

        Request<List<HumProxy>> overview();

        Request<List<HumProxy>> yesterday();

        Request<List<HumProxy>> lastDecade();
    }

    @Service(value = UserService.class, locator = InjectingServiceLocator.class)
    interface UserRequest extends RequestContext {
        Request<UserProxy> me();

        Request<Void> signOut();
    }
}