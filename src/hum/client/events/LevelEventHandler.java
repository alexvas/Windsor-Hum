package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LevelEventHandler extends EventHandler {
    void dispatch(LevelEvent event);
}
