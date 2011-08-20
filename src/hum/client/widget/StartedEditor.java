package hum.client.widget;

import hum.client.ModeHolder;
import hum.client.events.StartedEvent;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
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
public class StartedEditor extends Composite implements LeafValueEditor<Date> {
    interface Binder extends UiBinder<Widget, StartedEditor> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private EventBus bus;

    @Inject
    private ModeHolder modeHolder;

    private boolean initialized = false;

    @UiField(provided = true)
    HourMinutePicker startedTime;

    @UiField
    DatePicker startedDate;

    @UiField
    Button startedNow;

    private Date current = null;

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
    }

    private void fireStarted() {
        current = startedDate.getValue();
        if (current == null) {
            current = new Date();
        }
        if (startedTime.getMinutes() == null) {
            current.setHours(0);
            current.setMinutes(0);
        } else {
            current.setHours(startedTime.getHour());
            current.setMinutes(startedTime.getMinute());
        }
        current.setSeconds(0);
        fireStarted(current);
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("startedNow")
    void startedNow(ClickEvent startedNow) {
        fireStarted(new Date());
    }

    private void fireStarted(Date date) {
        bus.fireEvent(new StartedEvent(date));
        modeHolder.userEvent();
    }

    @Override
    public void setValue(Date value) {
        current = value;
        startedDate.setValue(value);
        if (value == null) {
            startedTime.clear();
        } else {
            startedTime.setTime(null, value.getHours(), value.getMinutes());
        }
    }

    @Override
    public Date getValue() {
        return current;
    }

}
