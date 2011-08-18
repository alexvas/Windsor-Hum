package hum.client.widget;

import hum.client.ReqFactory;
import hum.client.adapter.JanrainWrapper;
import hum.client.events.MeEvent;
import hum.client.events.MeEventHandler;
import hum.client.events.ModeEvent;
import hum.client.events.ModeEventHandler;
import hum.client.model.InfoProxy;
import hum.client.model.UserProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;

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

    @UiField
    HTML mapPlace;

    @UiField
    HTMLPanel about;

    @UiField
    Button report;

    @UiField
    HeadingElement userName;

    @UiField
    ImageElement avatar;

    @UiField
    SpanElement signOutWrapper;

    @UiField
    Anchor signOut;

    @UiField
    DeckLayoutPanel deck;

    @Inject
    @UiField(provided = true)
    HumUiEditor humUiEditor;

    @Inject
    @UiField(provided = true)
    Summary summary;

    boolean initialized = false;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        humUiEditor.init();
        summary.init();
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
        janrainWrapper.show(callback, "windsorhum.rpxnow.com");
    }

    @Override
    public void dispatch(MeEvent meEvent) {
        setUser(meEvent.user);
    }

    private void setUser(UserProxy user) {
        if (user == null) {
            userName.setInnerText(null);
            signOutWrapper.getStyle().setDisplay(Style.Display.NONE);
            avatar.setSrc(null);
            avatar.setAlt(null);
            report.setVisible(true);
            return;
        }
        InfoProxy info = user.getInfo().get(0);
        userName.setInnerText("Welcome, " + info.getDisplayName() + "!");
        avatar.setAlt(info.getDisplayName());
        avatar.setSrc(info.getPhoto());
        avatar.setHeight(72);
        report.setVisible(false);
        signOutWrapper.getStyle().setDisplay(Style.Display.INLINE);
    }

    @Override
    public void dispatch(ModeEvent event) {
        setMode(event.mode);
    }

    private void setMode(Mode mode) {
        switch (mode) {
            case NEW: // fall through
            case LAST:
                deck.showWidget(1);
                break;
            case LIST:
                deck.showWidget(0);
                break;
            default:
                throw new RuntimeException("mode not supported: " + mode);
        }
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("signOut")
    void signOut(ClickEvent signOut) {
        reqFactory.userRequest().signOut().fire(new Receiver<Void>() {
            @Override
            public void onSuccess(Void response) {
//                janrainWrapper.signOut();
                bus.fireEvent(new MeEvent(null));
                bus.fireEvent(new ModeEvent(Mode.LIST));
            }
        });
    }
}
