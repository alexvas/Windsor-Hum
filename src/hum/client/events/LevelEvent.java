package hum.client.events;

import hum.client.model.HumProxy;

import com.google.web.bindery.event.shared.Event;

public class LevelEvent extends Event<LevelEventHandler> {
    public final static Type<LevelEventHandler> TYPE = new Type<LevelEventHandler>();
    public final HumProxy.Level level;

    public LevelEvent(HumProxy.Level level) {
        this.level = level;
    }

    @Override
    public Type<LevelEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LevelEventHandler handler) {
        handler.dispatch(this);
    }
}
