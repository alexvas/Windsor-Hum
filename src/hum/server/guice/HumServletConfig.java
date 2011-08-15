package hum.server.guice;

import hum.server.adapter.janrain.JanrainCallbackServlet;
import hum.server.services.EventService;
import hum.server.services.EventServiceImpl;
import hum.server.services.UserService;
import hum.server.services.UserServiceImpl;

import com.allen_sauer.gwt.log.server.RemoteLoggerServlet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class HumServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new Misc(),
                new RequestFactoryInjectingModule("/gwtRequest")
        );
    }

    private static class Misc extends ServletModule {
        @Override
        protected void configureServlets() {
            // bind
            bind(RemoteLoggerServlet.class).in(Singleton.class);
            bind(JanrainCallbackServlet.class).in(Singleton.class);

            bind(EventService.class).to(EventServiceImpl.class).in(Singleton.class);
            bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);

            // configure URLs
            serve("*/gwt-log").with(RemoteLoggerServlet.class);
            serve("*/" + JanrainCallbackServlet.JANRAIN_CALLBACK).with(JanrainCallbackServlet.class);
        }
    }
}
