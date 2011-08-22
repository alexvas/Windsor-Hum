package hum.server.guice;

import hum.client.adapter.janrain.JanrainWrapper;
import hum.server.DownloadServlet;
import hum.server.ResetMemcacheServlet;
import hum.server.adapter.janrain.JanrainCallbackServlet;
import hum.server.services.HumService;
import hum.server.services.HumServiceImpl;
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

            bind(HumService.class).to(HumServiceImpl.class).in(Singleton.class);
            bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);

            // configure URLs
            serve("*/gwt-log").with(RemoteLoggerServlet.class);
            serve(JanrainWrapper.JANRAIN_CALLBACK).with(JanrainCallbackServlet.class);
            serve("/resetMemcacheNow").with(ResetMemcacheServlet.class);
            serve("/download*").with(DownloadServlet.class);
        }
    }
}
