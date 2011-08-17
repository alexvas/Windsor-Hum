package hum.client.events;

import hum.client.model.AddressProxy;

import com.google.web.bindery.event.shared.Event;

public class AddressEvent extends Event<AddressEventHandler> {
    public final static Type<AddressEventHandler> TYPE = new Type<AddressEventHandler>();

    public final AddressProxy address;

    public AddressEvent(AddressProxy address) {
        this.address = address;
    }

    @Override
    public Type<AddressEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddressEventHandler handler) {
        handler.dispatch(this);
    }
}
