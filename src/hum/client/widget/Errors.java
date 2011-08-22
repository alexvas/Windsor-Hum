package hum.client.widget;

import static hum.client.Utils.UTILS;
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
import com.google.gwt.dom.client.Style;
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

    private GQuery current;

    private enum State {VISIBLE, FADE_IN, FADE_OUT, HIDDEN}

    private State state = State.HIDDEN;

    private final Function onFadeIn = new Function() {
        @Override
        public void f() {
            current = null;
            state = State.VISIBLE;
        }
    };

    private Function onFadeOut = new Function() {
        @Override
        public void f() {
            errors.setText(null);
            current = null;
            state = State.HIDDEN;
        }
    };

    @Override
    public void dispatch(ErrorEvent event) {
        errors.setText(UTILS.join("; ", event.violations));
        switch (state) {
            case VISIBLE:
                return;
            case FADE_IN:
                return;
            case FADE_OUT:
                current.stop();
                break;
            case HIDDEN:
                break;
            default:
                throw new RuntimeException("state " + state + " is not supported");
        }
        state = State.FADE_IN;
        resetStyle();
        current = GQuery.$(errors).fadeIn(2000, onFadeIn);
    }

    private void clear() {
        switch (state) {
            case VISIBLE:
                break;
            case FADE_IN:
                current.stop();
                break;
            case FADE_OUT:
                return;
            case HIDDEN:
                return;
            default:
                throw new RuntimeException("state " + state + " is not supported");
        }
        state = State.FADE_OUT;
        resetStyle();
        GQuery.$(errors).fadeOut(1000, onFadeOut);
    }

    private void resetStyle() {
        Style style = errors.getElement().getStyle();
        style.clearOpacity();
        style.clearVisibility();
    }
}
