package hum.client;

import hum.client.events.AddressEvent;
import hum.client.events.AddressEventHandler;
import hum.client.model.AddressProxy;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class AddressEditor implements LeafValueEditor<AddressProxy>, AddressEventHandler {
    private final EventBus bus;

    private AddressProxy currentAddress;

    @Inject
    public AddressEditor(EventBus bus) {
        this.bus = bus;
        bus.addHandler(AddressEvent.TYPE, this);
    }

    @Override
    public void dispatch(AddressEvent event) {
        setAddress(event.address);
    }

    private void setAddress(AddressProxy address) {
        currentAddress = address;
    }

    @Override
    public void setValue(AddressProxy value) {
        currentAddress = value;
        bus.fireEvent(new AddressEvent(currentAddress));
    }

    @Override
    public AddressProxy getValue() {
        return currentAddress;
    }
}
