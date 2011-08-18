package hum.client.events;

import hum.client.model.PointProxy;

import com.google.web.bindery.event.shared.Event;

public class PointEvent extends Event<PointEventHandler> {
    public final static Type<PointEventHandler> TYPE = new Type<PointEventHandler>();

    public final PointProxy point;

    public PointEvent(PointProxy point) {
        this.point = point;
    }

    @Override
    public Type<PointEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PointEventHandler handler) {
        handler.dispatch(this);
    }
}
