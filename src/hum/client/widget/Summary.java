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
import hum.client.model.AddressProxy;
import hum.client.model.PointProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
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
        LevelEventHandler, PositionEventHandler, AddressEventHandler, ModeEventHandler,
        IsEditor<LeafValueEditor<AddressProxy>>, LeafValueEditor<PointProxy>
{

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

    private PointProxy currentPoint;

    private AddressProxy currentAddress;

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
        setPoint(event.point);
    }

    private void setPoint(PointProxy point) {
        currentPoint = point;
        if (currentPoint == null) {
            lat.setInnerText(null);
            lng.setInnerText(null);
        } else {
            lat.setInnerText(COORD_FORMAT.format(currentPoint.getLat()));
            lng.setInnerText(COORD_FORMAT.format(currentPoint.getLng()));
        }
    }

    @Override
    public void dispatch(AddressEvent event) {
        setAddress(event.address);
    }

    private void setAddress(AddressProxy address) {
        currentAddress = address;
        this.address.setInnerText(
                currentAddress == null
                        ? null
                        : CLIENT_UTILS.join(
                        ", ",
                        currentAddress.getCountry(),
                        currentAddress.getRegion(),
                        currentAddress.getPostcode(),
                        currentAddress.getAddressLine()
                ));
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

    private AddressProxyWrapper addressProxyWrapper = new AddressProxyWrapper();

    @Override
    public LeafValueEditor<AddressProxy> asEditor() {
        return addressProxyWrapper;
    }

    @Override
    public void setValue(PointProxy value) {
        setPoint(value);
    }

    @Override
    public PointProxy getValue() {
        return currentPoint;
    }

    private class AddressProxyWrapper implements LeafValueEditor<AddressProxy> {
        @Override
        public void setValue(AddressProxy value) {
            setAddress(value);
        }

        @Override
        public AddressProxy getValue() {
            return currentAddress;
        }
    }

}
