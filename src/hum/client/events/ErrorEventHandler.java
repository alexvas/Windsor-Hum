package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface ErrorEventHandler extends EventHandler {
    void dispatch(ErrorEvent event);
}
