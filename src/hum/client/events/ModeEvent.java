package hum.client.events;

import com.google.web.bindery.event.shared.Event;

public class ModeEvent extends Event<ModeEventHandler> {
    public final static Type<ModeEventHandler> TYPE = new Type<ModeEventHandler>();

    @Override
    public Type<ModeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ModeEventHandler handler) {
        handler.dispatch(this);
    }
}
