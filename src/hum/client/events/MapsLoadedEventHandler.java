package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface MapsLoadedEventHandler extends EventHandler {
    void dispatch(MapsLoadedEvent event);
}
