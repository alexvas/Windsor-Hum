package hum.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface AddressEventHandler extends EventHandler {
    void dispatch(AddressEvent event);
}
