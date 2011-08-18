package hum.client.widget;

import hum.client.events.StartedEvent;
import hum.client.events.StartedEventHandler;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@SuppressWarnings({"deprecation"})
@Singleton
public class StartedEditor extends Composite implements StartedEventHandler {

    interface Binder extends UiBinder<Widget, StartedEditor> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private EventBus bus;

    boolean initialized = false;

    @UiField(provided = true)
    HourMinutePicker startedTime;

    @UiField
    DatePicker startedDate;

    @UiField
    Button startedNow;

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

        bus.addHandler(StartedEvent.TYPE, this);
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
        startedTime.setTime(null, meEvent.started.getHours(), meEvent.started.getMinutes());
    }
}
