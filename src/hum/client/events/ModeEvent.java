package hum.client.events;

import hum.client.Mode;

import com.google.web.bindery.event.shared.Event;

public class ModeEvent extends Event<ModeEventHandler> {
    public final static Type<ModeEventHandler> TYPE = new Type<ModeEventHandler>();

    public final Mode mode;

    public ModeEvent(Mode mode) {
        this.mode = mode;
    }

    @Override
    public Type<ModeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ModeEventHandler handler) {
        handler.dispatch(this);
    }
}
