package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface ModeEventHandler extends EventHandler {
    void dispatch(ModeEvent event);
}
