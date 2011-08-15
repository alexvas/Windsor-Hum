package hum.server.guice;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;

/**
 * InjectingServiceDecorator is a ServiceLayerDecorator that uses DI to inject
 * service, entities and the JSR 303 validator.
 */
public class InjectingServiceDecorator extends ServiceLayerDecorator {
    /**
     * JSR 303 validator used to validate requested entities.
     */
    @Inject
    private Validator validator;
    @Inject
    private Injector injector;

    /**
     * Uses Guice to create the instance of the target locator, so the locator implementation could be injected.
     */
    @Override
    public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    @Override
    public Object createServiceInstance(Method contextMethod, Method domainMethod) {
        Class<?> serviceClazz = domainMethod.getDeclaringClass();
        return injector.getInstance(serviceClazz);
    }


    /**
     * Invokes JSR 303 validator on a given domain object.
     *
     * @param domainObject the domain object to be validated
     * @param <T>          the type of the entity being validated
     * @return the violations associated with the domain object
     */
    @Override
    public <T> Set<ConstraintViolation<T>> validate(T domainObject) {
        return validator.validate(domainObject);
    }


}
