package hum.client.widget;

import static hum.client.ClientUtils.CLIENT_UTILS;
import hum.client.events.LevelEvent;
import hum.client.events.LevelEventHandler;
import hum.client.events.StartedEvent;
import hum.client.events.StartedEventHandler;
import hum.client.model.HumProxy;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class HumInstanceEditor extends Composite implements StartedEventHandler, LevelEventHandler {

    interface Binder extends UiBinder<Widget, HumInstanceEditor> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private EventBus bus;

    boolean initialized = false;

    @UiField
    SpanElement address;

    @UiField
    SpanElement lat;

    @UiField
    SpanElement lng;

    @UiField
    SpanElement summaryStarted;

    @UiField
    SpanElement summaryLevel;

    @UiField
    TextBox zip;

    @UiField(provided = true)
    HourMinutePicker startedTime;

    @UiField
    ListBox level;

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

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        startedTime = new HourMinutePicker(HourMinutePicker.PickerFormat._12_HOUR, 0, 23, 6);
        initWidget(binder.createAndBindUi(this));
        level.addItem("Please select... ", "");
        for (HumProxy.Level le : HumProxy.Level.values()) {
            level.addItem(CLIENT_UTILS.capitalize(le.name()), le.name());
        }
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
        level.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                try {
                    bus.fireEvent(new LevelEvent(HumProxy.Level.valueOf(
                            level.getValue(level.getSelectedIndex())
                    )));
                } catch (IllegalArgumentException ignored) {
                    // do nothing
                }
            }
        });
        bus.addHandler(StartedEvent.TYPE, this);
        bus.addHandler(LevelEvent.TYPE, this);
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
        summaryStarted.setInnerText(
                DateTimeFormat
                        .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_LONG)
                        .format(meEvent.started)
        );
    }

    @Override
    public void dispatch(LevelEvent event) {
        for (int i = 0; i < level.getItemCount(); ++i) {
            if (event.level.name().equals(level.getItemText(i))) {
                level.setSelectedIndex(i);
                break;
            }
        }
        summaryLevel.setInnerText(CLIENT_UTILS.capitalize(event.level.name()));
    }
}
