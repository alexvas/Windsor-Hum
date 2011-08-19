package hum.client.widget;

import hum.client.LevelHelper;
import hum.client.events.LevelEvent;
import hum.client.events.MapsLoadedEvent;
import hum.client.events.MapsLoadedEventHandler;
import hum.client.model.HumProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class LevelEditor extends Composite implements MapsLoadedEventHandler, LeafValueEditor<HumProxy.Level> {

    interface Binder extends UiBinder<Widget, LevelEditor> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private EventBus bus;

    @Inject
    private LevelHelper levelHelper;

    private boolean initialized = false;

    @UiField
    RadioButton levelLow;

    @UiField
    RadioButton levelMedium;

    @UiField
    RadioButton levelHigh;

    private HumProxy.Level current = null;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));

        addClickListener(levelHigh, HumProxy.Level.HIGH);
        addClickListener(levelMedium, HumProxy.Level.MEDIUM);
        addClickListener(levelLow, HumProxy.Level.LOW);

        bus.addHandler(MapsLoadedEvent.TYPE, this);
    }

    @Override
    public void dispatch(MapsLoadedEvent event) {
        init();
        setLiIcon(levelHigh, HumProxy.Level.HIGH);
        setLiIcon(levelMedium, HumProxy.Level.MEDIUM);
        setLiIcon(levelLow, HumProxy.Level.LOW);
    }

    private void addClickListener(RadioButton button, final HumProxy.Level level) {
        GQuery.$(button).parent("li").click(new Function() {
            @Override
            public void f() {
                setValue(level);
                bus.fireEvent(new LevelEvent(level));
            }
        });
    }

    private void setLiIcon(RadioButton button, HumProxy.Level level) {
        GQuery.$(button).parent("li")
                .find("img").get(0).<ImageElement>cast().setSrc(levelHelper.icon(level).getIcon().url());
    }

    @Override
    public void setValue(HumProxy.Level value) {
        current = value;
        if (value == null) {
            levelLow.setValue(false);
            levelMedium.setValue(false);
            levelHigh.setValue(false);
            return;
        }
        switch (value) {
            case LOW:
                levelLow.setValue(true);
                break;
            case MEDIUM:
                levelMedium.setValue(true);
                break;
            case HIGH:
                levelHigh.setValue(true);
                break;
            default:
                throw new RuntimeException("level " + value + " is not supported");
        }
    }

    @Override
    public HumProxy.Level getValue() {
        return current;
    }
}
