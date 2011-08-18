package hum.client.events;

import com.google.web.bindery.event.shared.Event;

public class WannaSaveEvent extends Event<WannaSaveEventHandler> {
    public final static Type<WannaSaveEventHandler> TYPE = new Type<WannaSaveEventHandler>();

    public WannaSaveEvent() {
    }

    @Override
    public Type<WannaSaveEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(WannaSaveEventHandler handler) {
        handler.dispatch(this);
    }
}
