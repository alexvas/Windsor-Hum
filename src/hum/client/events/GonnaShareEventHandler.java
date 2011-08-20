package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface GonnaShareEventHandler extends EventHandler {
    void dispatch(GonnaShareEvent event);
}
