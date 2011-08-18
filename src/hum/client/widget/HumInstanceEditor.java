package hum.client.widget;

import hum.client.LevelHelper;
import hum.client.events.LevelEvent;
import hum.client.events.LevelEventHandler;
import hum.client.events.MapsLoadedEvent;
import hum.client.events.MapsLoadedEventHandler;
import hum.client.maps.geocoder.GeocoderService;
import hum.client.model.HumProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.query.client.Function;
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
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@SuppressWarnings({"deprecation"})
@Singleton
public class HumInstanceEditor extends Composite implements LevelEventHandler, MapsLoadedEventHandler {

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

    @UiField
    StackLayoutPanel stack;

    @UiField
    TextArea comment;

    @UiField
    Button share;

    @UiField
    Button go;

    @UiField
    RadioButton levelLow;

    @UiField
    RadioButton levelMedium;

    @UiField
    RadioButton levelHigh;

    @Inject
    @UiField(provided = true)
    StartedEditor started;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        started.init();
        initWidget(binder.createAndBindUi(this));

        addClickListener(levelHigh, HumProxy.Level.HIGH);
        addClickListener(levelMedium, HumProxy.Level.MEDIUM);
        addClickListener(levelLow, HumProxy.Level.LOW);

        zip.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                geocode();
            }
        });
        bus.addHandler(LevelEvent.TYPE, this);
        bus.addHandler(MapsLoadedEvent.TYPE, this);
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

    private void addClickListener(RadioButton button, final HumProxy.Level level) {
        final GQuery li = GQuery.$(button).parent("li");
        li.click(new Function() {
            @Override
            public void f() {
                InputElement in = li.find("input").get(0).cast();
                in.setChecked(true);
                bus.fireEvent(new LevelEvent(level));
            }
        });
    }

    private void setLiIcon(RadioButton button, HumProxy.Level level) {
        final GQuery li = GQuery.$(button).parent("li");
        li.find("img").get(0).<ImageElement>cast().setSrc(levelHelper.icon(level).getIcon().url());
    }
}
