package hum.server.guice;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

/**
 * RequestFactoryInjectingModule contains configuration of the RequestFactory injection mechanism.
 *
 * @author Miroslav Genov (mgenov@gmail.com)
 * @author Alexander Vasiljev (a.a.vasiljev@gmail.com)
 */
class RequestFactoryInjectingModule extends ServletModule {

    private final String targetUrl;

    public RequestFactoryInjectingModule(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    protected void configureServlets() {
        serve(targetUrl).with(RequestFactoryServlet.class);
    }

    /**
     * Creates and reuses injecting JSR 303 Validator.
     *
     * @param injector injector
     * @return JSR 303 Validator
     */
    @Provides
    @Singleton
    Validator getValidator(final Injector injector) {
        return Validation.byDefaultProvider()
                .configure()
                .constraintValidatorFactory(new ConstraintValidatorFactory() {
                    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> clazz) {
                        return injector.getInstance(clazz);
                    }
                })
                .buildValidatorFactory()
                .getValidator();
    }

    @Provides
    @Singleton
    @Inject
    RequestFactoryServlet servlet(InjectingServiceDecorator serviceLayerDecorator) {
        return new RequestFactoryServlet(new DefaultExceptionHandler(), serviceLayerDecorator);
    }

}
