package hum.client.events;

import com.google.web.bindery.event.shared.Event;

public class MapsLoadedEvent extends Event<MapsLoadedEventHandler> {
    public final static Type<MapsLoadedEventHandler> TYPE = new Type<MapsLoadedEventHandler>();

    @Override
    public Type<MapsLoadedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MapsLoadedEventHandler handler) {
        handler.dispatch(this);
    }
}
