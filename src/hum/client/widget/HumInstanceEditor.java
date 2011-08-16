package hum.client.widget;

import hum.client.ReqFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.summatech.gwt.client.HourMinutePicker;

@Singleton
public class HumInstanceEditor extends Composite {

    interface Binder extends UiBinder<Widget, HumInstanceEditor> {
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
    SpanElement time;

    @UiField
    SpanElement summaryLevel;

    @UiField
    TextBox zip;

    @UiField(provided = true)
    HourMinutePicker hourMinutePicker;

    @UiField
    ListBox level;

    @UiField
    DatePicker datePicker;

    @UiField
    StackLayoutPanel stack;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        hourMinutePicker = new HourMinutePicker(HourMinutePicker.PickerFormat._12_HOUR, 0, 23, 6);
        initWidget(binder.createAndBindUi(this));
        hourMinutePicker.setTime("am", 11, 0);
    }
}
