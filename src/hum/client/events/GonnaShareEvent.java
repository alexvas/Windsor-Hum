package hum.client.events;

import com.google.web.bindery.event.shared.Event;

public class GonnaShareEvent extends Event<GonnaShareEventHandler> {
    public final static Type<GonnaShareEventHandler> TYPE = new Type<GonnaShareEventHandler>();

    @Override
    public Type<GonnaShareEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GonnaShareEventHandler handler) {
        handler.dispatch(this);
    }
}
