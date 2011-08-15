package hum.server.guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
public class InjectingServiceLocator implements ServiceLocator {
    @Inject
    private Injector injector;

    public Object getInstance(Class<?> clazz) {
        return injector.getInstance(clazz);
    }
}
