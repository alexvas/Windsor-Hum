package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface MeEventHandler extends EventHandler {
    void dispatch(MeEvent event);
}
