package hum.client.widget;

import hum.client.LevelHelper;
import hum.client.maps.geocoder.GeocoderService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@SuppressWarnings({"deprecation"})
@Singleton
public class HumInstanceEditor extends Composite {

    interface Binder extends UiBinder<Widget, HumInstanceEditor> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private GeocoderService geocoder;

    @Inject
    private LevelHelper levelHelper;

    boolean initialized = false;

    @UiField
    TextBox zip;

    @UiField
    StackLayoutPanel stack;

    @UiField
    TextArea comment;

    @UiField
    Button share;

    @UiField
    Button go;

    @Inject
    @UiField(provided = true)
    StartedEditor started;

    @Inject
    @UiField(provided = true)
    LevelEditor level;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        started.init();
        level.init();
        initWidget(binder.createAndBindUi(this));

        zip.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                geocode();
            }
        });
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("go")
    void go(ClickEvent go) {
        geocode();
    }

    private void geocode() {
        geocoder.direct(zip.getText());
    }
}
