package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface PositionEventHandler extends EventHandler {
    void dispatch(PositionEvent event);
}
