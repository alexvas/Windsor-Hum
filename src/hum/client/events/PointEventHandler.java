package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface PointEventHandler extends EventHandler {
    void dispatch(PointEvent event);
}
