package hum.client.events;

import java.util.Date;

import com.google.web.bindery.event.shared.Event;

public class StartedEvent extends Event<StartedEventHandler> {
    public final static Type<StartedEventHandler> TYPE = new Type<StartedEventHandler>();

    public final Date started;

    public StartedEvent(Date started) {
        this.started = started;
    }

    @Override
    public Type<StartedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StartedEventHandler handler) {
        handler.dispatch(this);
    }
}
