package hum.client.events;

import hum.client.model.PointProxy;

import com.google.web.bindery.event.shared.Event;

public class PositionEvent extends Event<PositionEventHandler> {
    public final static Type<PositionEventHandler> TYPE = new Type<PositionEventHandler>();

    public final PointProxy point;

    public PositionEvent(PointProxy point) {
        this.point = point;
    }

    @Override
    public Type<PositionEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PositionEventHandler handler) {
        handler.dispatch(this);
    }
}
