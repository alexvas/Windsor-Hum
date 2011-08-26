package hum.client.widget;

import hum.client.ModeHolder;
import static hum.client.TimeHelper.TIME_HELPER;
import static hum.client.Utils.UTILS;
import hum.client.events.StartedEvent;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
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

    @UiField
    DatePicker startedDate;

    @UiField
    Button startedNow;

    @UiField
    ListBox hours;

    @UiField
    ListBox minutes;

    @UiField
    ListBox ampm;

    private ChangeHandler onTimeChanged = new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
            fireStarted2();
        }
    };

    private Date local = null;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));
        for (int i = 0; i < 13; ++i) {
            hours.addItem(twoDigitFormat(i));
        }

        for (int i = 0; i < 60; i += 5) {
            minutes.addItem(twoDigitFormat(i));
        }

        ampm.addItem("AM");
        ampm.addItem("PM");

        startedDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
                fireStarted2();
            }
        });
        hours.addChangeHandler(onTimeChanged);
        minutes.addChangeHandler(onTimeChanged);
        ampm.addChangeHandler(onTimeChanged);
    }

    private void fireStarted2() {
        Date local = startedDate.getValue();
        if (local == null) {
            local = new Date();
        }
        local.setHours(
                hours.getSelectedIndex() < 0
                        ? 0
                        : Integer.valueOf(hours.getValue(hours.getSelectedIndex()))
        );
        local.setMinutes(
                minutes.getSelectedIndex() < 0
                        ? 0
                        : Integer.valueOf(minutes.getValue(minutes.getSelectedIndex()))
        );
        if (ampm.getSelectedIndex() > 0) {
            local.setHours(local.getHours() + 12);
        }
        local.setSeconds(0);
        fireStarted(local);
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("startedNow")
    void startedNow(ClickEvent startedNow) {
        fireStarted(new Date());
    }

    private void fireStarted(Date local) {
        Date utc = TIME_HELPER.toGmt(local);
        bus.fireEvent(new StartedEvent(utc));
        modeHolder.userEvent();
        setValue(utc);
    }

    @Override
    public void setValue(Date utc) {
        local = TIME_HELPER.fromGmt(utc);
        startedDate.setValue(local);
        if (local == null) {
            hours.setSelectedIndex(-1);
            minutes.setSelectedIndex(-1);
            ampm.setSelectedIndex(-1);
        } else {
            int h = local.getHours();
            int m = local.getMinutes();
            setHoursDropbox(h);
            setMinutesDropbox(m);
        }
    }

    private void setHoursDropbox(int h) {
        if (h == 12) {
            hours.setSelectedIndex(hours.getItemCount() - 1);
            ampm.setSelectedIndex(1);
            return;
        }
        for (int i = 0; i < hours.getItemCount(); ++i) {
            if (Integer.parseInt(hours.getValue(i)) == h) {
                hours.setSelectedIndex(i);
                ampm.setSelectedIndex(0);
                return;
            }
        }
        h -= 12;
        for (int i = 0; i < hours.getItemCount(); ++i) {
            if (Integer.parseInt(hours.getValue(i)) == h) {
                hours.setSelectedIndex(i);
                ampm.setSelectedIndex(1);
                return;
            }
        }
    }

    private void setMinutesDropbox(int m) {
        int rounded = 5 * UTILS.round(m / 5.0);
        for (int i = 0; i < minutes.getItemCount(); ++i) {
            if (Integer.parseInt(minutes.getValue(i)) == rounded) {
                minutes.setSelectedIndex(i);
                return;
            }
        }
    }

    @Override
    public Date getValue() {
        return TIME_HELPER.toGmt(local);
    }

    private String twoDigitFormat(int i) {
        return i < 10
                ? "0" + i
                : "" + i;
    }
}
