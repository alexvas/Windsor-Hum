package hum.client.events;

import hum.client.model.UserProxy;

import com.google.web.bindery.event.shared.Event;

public class MeEvent extends Event<MeEventHandler> {
    public final static Type<MeEventHandler> TYPE = new Type<MeEventHandler>();

    public final UserProxy user;

    public MeEvent(UserProxy user) {
        this.user = user;
    }

    @Override
    public Type<MeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MeEventHandler handler) {
        handler.dispatch(this);
    }
}
