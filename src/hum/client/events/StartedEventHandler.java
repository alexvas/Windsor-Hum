package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface StartedEventHandler extends EventHandler {
    void dispatch(StartedEvent meEvent);
}
