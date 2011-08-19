package hum.client.events;

import hum.client.model.HumProxy;

import java.util.List;

import com.google.web.bindery.event.shared.Event;

public class OverviewEvent extends Event<OverviewEventHandler> {
    public final static Type<OverviewEventHandler> TYPE = new Type<OverviewEventHandler>();

    public final List<HumProxy> hums;

    public OverviewEvent(List<HumProxy> hums) {
        this.hums = hums;
    }

    @Override
    public Type<OverviewEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(OverviewEventHandler handler) {
        handler.dispatch(this);
    }
}
