package hum.client.widget;

import static hum.client.ClientUtils.CLIENT_UTILS;
import hum.client.ReqFactory;
import hum.client.events.AddressEvent;
import hum.client.events.AddressEventHandler;
import hum.client.events.LevelEvent;
import hum.client.events.LevelEventHandler;
import hum.client.events.ModeEvent;
import hum.client.events.ModeEventHandler;
import hum.client.events.PositionEvent;
import hum.client.events.PositionEventHandler;
import hum.client.events.StartedEvent;
import hum.client.events.StartedEventHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class Summary extends Composite implements StartedEventHandler,
        LevelEventHandler, PositionEventHandler, AddressEventHandler, ModeEventHandler {

    interface Binder extends UiBinder<Widget, Summary> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private EventBus bus;

    @Inject
    private ReqFactory reqFactory;

    boolean initialized = false;

    @UiField
    SpanElement address;

    @UiField
    SpanElement lat;

    @UiField
    SpanElement lng;

    @UiField
    SpanElement started;

    @UiField
    SpanElement level;

    @UiField
    ParagraphElement status;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));
        bus.addHandler(StartedEvent.TYPE, this);
        bus.addHandler(LevelEvent.TYPE, this);
        bus.addHandler(PositionEvent.TYPE, this);
        bus.addHandler(AddressEvent.TYPE, this);
        bus.addHandler(ModeEvent.TYPE, this);
    }

    @Override
    public void dispatch(StartedEvent meEvent) {
        started.setInnerText(
                DateTimeFormat
                        .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_LONG)
                        .format(meEvent.started)
        );
        sendHum();
    }

    @Override
    public void dispatch(LevelEvent event) {
        level.setInnerText(CLIENT_UTILS.capitalize(event.level.name()));
        sendHum();
    }

    private static final NumberFormat COORD_FORMAT = NumberFormat.getFormat("###.######");

    @Override
    public void dispatch(PositionEvent event) {
        if (event.point == null) {
            lat.setInnerText(null);
            lng.setInnerText(null);
        } else {
            lat.setInnerText(COORD_FORMAT.format(event.point.getLat()));
            lng.setInnerText(COORD_FORMAT.format(event.point.getLng()));
        }
        sendHum();
    }

    @Override
    public void dispatch(AddressEvent event) {
        address.setInnerText(
                event.address == null
                        ? null
                        : CLIENT_UTILS.join(
                        ", ",
                        event.address.getCountry(),
                        event.address.getRegion(),
                        event.address.getPostcode(),
                        event.address.getAddressLine()
                ));
        sendHum();
    }

    @Override
    public void dispatch(ModeEvent event) {
        switch (event.mode) {
            case NEW:
                status.setInnerText("not saved");
                break;
            case LAST:
                status.setInnerText("saved");
                break;
            case LIST:
                // do nothing
                break;
            default:
                break;
        }
    }

    private void sendHum() {
/*
        humRequest.save(hum).fire(new Receiver<Void>() {
            @Override
            public void onSuccess(Void response) {
                bus.fireEvent(new ModeEvent(Mode.LAST));
            }
        });
*/
    }
}
