package hum.client.widget;

import hum.client.LevelHelper;
import hum.client.events.LevelEvent;
import hum.client.events.LevelEventHandler;
import hum.client.events.MapsLoadedEvent;
import hum.client.events.MapsLoadedEventHandler;
import hum.client.events.StartedEvent;
import hum.client.events.StartedEventHandler;
import hum.client.maps.geocoder.GeocoderService;
import hum.client.model.HumProxy;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@SuppressWarnings({"deprecation"})
@Singleton
public class HumInstanceEditor extends Composite implements StartedEventHandler, LevelEventHandler,
        MapsLoadedEventHandler {

    interface Binder extends UiBinder<Widget, HumInstanceEditor> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private EventBus bus;

    @Inject
    private GeocoderService geocoder;

    @Inject
    private LevelHelper levelHelper;

    boolean initialized = false;

    @UiField
    TextBox zip;

    @UiField(provided = true)
    HourMinutePicker startedTime;

    @UiField
    DatePicker startedDate;

    @UiField
    StackLayoutPanel stack;

    @UiField
    TextArea comment;

    @UiField
    Button share;

    @UiField
    Button startedNow;

    @UiField
    Button go;

    @UiField
    RadioButton levelLow;

    @UiField
    RadioButton levelMedium;

    @UiField
    RadioButton levelHigh;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        startedTime = new HourMinutePicker(HourMinutePicker.PickerFormat._12_HOUR, 0, 23, 6);
        initWidget(binder.createAndBindUi(this));
        startedDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
                fireStarted();
            }
        });
        startedTime.addValueChangeHandler(new ValueChangeHandler<Integer>() {
            @Override
            public void onValueChange(ValueChangeEvent<Integer> integerValueChangeEvent) {
                fireStarted();
            }
        });

        levelHigh.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                bus.fireEvent(new LevelEvent(HumProxy.Level.HIGH));
            }
        });

        levelMedium.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                bus.fireEvent(new LevelEvent(HumProxy.Level.MEDIUM));
            }
        });

        levelLow.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                bus.fireEvent(new LevelEvent(HumProxy.Level.LOW));
            }
        });

        zip.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                geocode();
            }
        });
        bus.addHandler(StartedEvent.TYPE, this);
        bus.addHandler(LevelEvent.TYPE, this);
        bus.addHandler(MapsLoadedEvent.TYPE, this);
    }

    private void fireStarted() {
        Date started = startedDate.getValue();
        if (started == null) {
            started = new Date();
        }
        if (startedTime.getMinutes() == null) {
            started.setHours(0);
            started.setMinutes(0);
        } else {
            started.setHours(startedTime.getHour());
            started.setMinutes(startedTime.getMinute());
        }
        started.setSeconds(0);
        fireStarted(started);
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("startedNow")
    void startedNow(ClickEvent startedNow) {
        fireStarted(new Date());
    }

    private void fireStarted(Date date) {
        bus.fireEvent(new StartedEvent(date));
    }

    @Override
    public void dispatch(StartedEvent meEvent) {
        startedDate.setValue(meEvent.started);
        startedTime.setTime("", meEvent.started.getHours(), meEvent.started.getMinutes());
    }

    @Override
    public void dispatch(LevelEvent event) {
        switch (event.level) {
            case LOW:
                levelLow.setFocus(true);
                break;
            case MEDIUM:
                levelMedium.setFocus(true);
                break;
            case HIGH:
                levelHigh.setFocus(true);
                break;
            default:
                throw new RuntimeException("level " + event.level + " is not supported");
        }
    }

    private static final NumberFormat COORD_FORMAT = NumberFormat.getFormat("###.######");

    @Override
    public void dispatch(MapsLoadedEvent event) {
        init();
        setLiIcon(levelHigh, HumProxy.Level.HIGH);
        setLiIcon(levelMedium, HumProxy.Level.MEDIUM);
        setLiIcon(levelLow, HumProxy.Level.LOW);
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("go")
    void go(ClickEvent go) {
        geocode();
    }

    private void geocode() {
        geocoder.direct(zip.getText());
    }

    private void setLiIcon(RadioButton button, HumProxy.Level level) {
        ImageElement i = GQuery.$(button).parent("li").find("img").get(0).cast();
        i.setSrc(levelHelper.icon(level).getIcon().url());
    }


}
