package hum.client.widget;

import static com.google.gwt.query.client.GQuery.$;
import hum.client.ModeHolder;
import hum.client.ReqFactory;
import hum.client.adapter.janrain.JanrainWrapper;
import hum.client.events.MeEvent;
import hum.client.events.MeEventHandler;
import hum.client.events.ModeEvent;
import hum.client.events.ModeEventHandler;
import hum.client.model.UserProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class Root extends Composite implements MeEventHandler, ModeEventHandler {

    interface Binder extends UiBinder<Widget, Root> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private JanrainWrapper janrainWrapper;

    @Inject
    private EventBus bus;

    @Inject
    private ReqFactory reqFactory;

    @Inject
    private ModeHolder modeHolder;

    @UiField
    HTML mapPlace;

    @UiField
    HTMLPanel about;

    @UiField
    Button report;

    @UiField
    DeckLayoutPanel deck;

    @UiField
    DockLayoutPanel editor;

    @Inject
    @UiField(provided = true)
    HumUiEditor humUiEditor;

    @Inject
    @UiField(provided = true)
    Summary summary;

    @Inject
    @UiField(provided = true)
    Profile profile;

    @Inject
    @UiField(provided = true)
    UserMenu userMenu;

    @Inject
    @UiField(provided = true)
    Errors errors;

    @UiField
    DivElement addthis;

    boolean initialized = false;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        humUiEditor.init();
        summary.init();
        profile.init();
        userMenu.init();
        errors.init();
        initWidget(binder.createAndBindUi(this));
        bus.addHandler(MeEvent.TYPE, this);
        bus.addHandler(ModeEvent.TYPE, this);
    }

    public com.google.gwt.user.client.Element getMapPlace() {
        init();
        return mapPlace.getElement().cast();
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("report")
    void report(ClickEvent report) {
        String callback = Window.Location
                .createUrlBuilder()
                .setPath(JanrainWrapper.JANRAIN_CALLBACK)
                .buildString();
        janrainWrapper.show(callback, JanrainWrapper.JANRAIN_DOMAIN);
//        janrainWrapper.afterLoadInitHack(callback, JanrainWrapper.JANRAIN_DOMAIN);
    }

    @Override
    public void dispatch(MeEvent meEvent) {
        setUser(meEvent.user);
    }

    private void setUser(UserProxy user) {
        if (user == null) {
            $(report).fadeIn(1000);
            $(addthis).fadeIn(1000);
        } else {
            $(report).fadeOut(1000);
            $(addthis).fadeOut(1000);
        }
    }

    @Override
    public void dispatch(ModeEvent event) {
        setMode();
    }

    private void setMode() {
        switch (modeHolder.mode()) {
            case NEW: // fall through
            case LAST: // fall through
            case UPDATED:
                deck.showWidget(editor);
                break;
            case LIST:
                deck.showWidget(about);
                break;
            default:
                throw new RuntimeException("mode not supported: " + modeHolder.mode());
        }
    }
}
