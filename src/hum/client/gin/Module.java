package hum.client.gin;

import hum.client.ReqFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class Module extends AbstractGinModule {

    public Module() {
        // entire if statement would be cut off by GWT Compiler if DebugLevel > Debug
        if (Log.isDebugEnabled()) {
            Log.debug(getClass().getName().replaceAll(".*\\.", "") + " created");
        }
    }

    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    }

    @Inject
    @Provides
    @Singleton
    ReqFactory reqFactory(EventBus bus) {
        ReqFactory factory = GWT.create(ReqFactory.class);
        factory.initialize(bus);
        return factory;
    }

    @Inject
    @Provides
    @Singleton
    ReqFactory.HumRequest humRequest(ReqFactory factory) {
        return factory.humRequest();
    }

    @Inject
    @Provides
    @Singleton
    ReqFactory.UserRequest userRequest(ReqFactory factory) {
        return factory.userRequest();
    }
}
