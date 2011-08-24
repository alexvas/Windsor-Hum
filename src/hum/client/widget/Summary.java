package hum.client.widget;

import static hum.client.ClientUtils.CLIENT_UTILS;
import hum.client.HumWorkflow;
import hum.client.ModeHolder;
import static hum.client.Utils.UTILS;
import hum.client.events.AddressEvent;
import hum.client.events.AddressEventHandler;
import hum.client.events.GonnaShareEvent;
import hum.client.events.LevelEvent;
import hum.client.events.LevelEventHandler;
import hum.client.events.ModeEvent;
import hum.client.events.ModeEventHandler;
import hum.client.events.PointEvent;
import hum.client.events.PointEventHandler;
import hum.client.events.StartedEvent;
import hum.client.events.StartedEventHandler;
import hum.client.model.AddressProxy;
import hum.client.model.HumProxy;
import hum.client.model.PointProxy;
import static hum.client.resources.Resources.RESOURCES;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class Summary extends Composite implements StartedEventHandler,
        LevelEventHandler, PointEventHandler, AddressEventHandler, ModeEventHandler {

    interface Binder extends UiBinder<Widget, Summary> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private EventBus bus;

    @Inject
    private HumWorkflow humWorkflow;

    @Inject
    private ModeHolder modeHolder;

    private boolean initialized = false;

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

    @UiField
    Button save;

    @UiField
    Button share;

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
    }

    @Override
    public void dispatch(StartedEvent meEvent) {
        setStarted(meEvent.local);
    }

    private void setStarted(Date started) {
        this.started.setInnerText(
                started == null
                        ? null
                        : DateTimeFormat
                        .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_LONG)
                        .format(started)
        );
    }

    @Override
    public void dispatch(LevelEvent event) {
        setLevel(event.level);
    }

    private void setLevel(HumProxy.Level level) {
        this.level.setInnerText(
                level == null
                        ? null
                        : UTILS.capitalize(level.name())
        );
    }

    @Override
    public void dispatch(PointEvent event) {
        setPoint(event.point);
    }

    private void setPoint(PointProxy point) {
        if (point == null) {
            lat.setInnerText(null);
            lng.setInnerText(null);
        } else {
            lat.setInnerText(CLIENT_UTILS.coordFormat.format(point.getLat()));
            lng.setInnerText(CLIENT_UTILS.coordFormat.format(point.getLng()));
        }
    }

    @Override
    public void dispatch(AddressEvent event) {
        setAddress(event.address);
    }

    private void setAddress(AddressProxy address) {
        this.address.setInnerText(
                address == null
                        ? null
                        : UTILS.join(
                        ", ",
                        address.getCountry(),
                        address.getRegion(),
                        address.getPostcode(),
                        address.getAddressLine()
                ));
    }

    @Override
    public void dispatch(ModeEvent event) {
        switch (modeHolder.mode()) {
            case NEW:
                status.setInnerText("draft");
                save.addStyleName(RESOURCES.style().hint());
                share.setVisible(false);
                break;
            case LAST:
                status.setInnerText("saved");
                save.removeStyleName(RESOURCES.style().hint());
                share.setVisible(true);
                break;
            case UPDATED:
                status.setInnerText("updated");
                save.addStyleName(RESOURCES.style().hint());
                share.setVisible(false);
                break;
            case LIST:
                // do nothing
                break;
            default:
                throw new RuntimeException("mode " + modeHolder.mode() + " is not supported");
        }
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("save")
    void save(ClickEvent save) {
        humWorkflow.save();
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("share")
    void share(ClickEvent share) {
        bus.fireEvent(new GonnaShareEvent());
    }

}
