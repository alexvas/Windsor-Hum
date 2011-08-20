package hum.client.events;

import hum.client.model.HumProxy;

import javax.validation.ConstraintViolation;

import com.google.web.bindery.event.shared.Event;

public class ErrorEvent extends Event<ErrorEventHandler> {
    public final static Type<ErrorEventHandler> TYPE = new Type<ErrorEventHandler>();

    public final Iterable<ConstraintViolation<HumProxy>> violations;

    public ErrorEvent(Iterable<ConstraintViolation<HumProxy>> violations) {
        this.violations = violations;
    }

    @Override
    public Type<ErrorEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ErrorEventHandler handler) {
        handler.dispatch(this);
    }
}
