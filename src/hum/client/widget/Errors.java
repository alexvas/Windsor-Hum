package hum.client.widget;

import static hum.client.ClientUtils.CLIENT_UTILS;
import hum.client.events.AddressEvent;
import hum.client.events.AddressEventHandler;
import hum.client.events.ErrorEvent;
import hum.client.events.ErrorEventHandler;
import hum.client.events.LevelEvent;
import hum.client.events.LevelEventHandler;
import hum.client.events.ModeEvent;
import hum.client.events.ModeEventHandler;
import hum.client.events.PointEvent;
import hum.client.events.PointEventHandler;
import hum.client.events.StartedEvent;
import hum.client.events.StartedEventHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class Errors extends Composite implements StartedEventHandler,
        LevelEventHandler, PointEventHandler, AddressEventHandler, ModeEventHandler, ErrorEventHandler {


    interface Binder extends UiBinder<Widget, Errors> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private EventBus bus;

    private boolean initialized = false;

    @UiField
    Label errors;

    private boolean showing = false;

    private Function clearText = new Function() {
        @Override
        public void f() {
            errors.setText(null);
        }
    };

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));
        bus.addHandler(StartedEvent.TYPE, this);
        bus.addHandler(LevelEvent.TYPE, this);
        bus.addHandler(PointEvent.TYPE, this);
        bus.addHandler(AddressEvent.TYPE, this);
        bus.addHandler(ModeEvent.TYPE, this);
        bus.addHandler(ErrorEvent.TYPE, this);
    }

    @Override
    public void dispatch(StartedEvent meEvent) {
        clear();
    }

    @Override
    public void dispatch(LevelEvent event) {
        clear();
    }

    @Override
    public void dispatch(PointEvent event) {
        clear();
    }

    @Override
    public void dispatch(AddressEvent event) {
        clear();
    }

    @Override
    public void dispatch(ModeEvent event) {
        clear();
    }

    private final Function dummy = new Function() {
        @Override
        public void f() {
        }
    };

    @Override
    public void dispatch(ErrorEvent event) {
        errors.setText(CLIENT_UTILS.join("; ", event.violations));
        if (showing) {
            return;
        }
        showing = true;
        GQuery.$(errors).fadeIn(2000, dummy);
    }

    private void clear() {
        if (!showing) {
            return;
        }
        showing = false;
        GQuery.$(errors).fadeOut(1000, clearText);
    }
}
