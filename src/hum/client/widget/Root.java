package hum.client.widget;

import hum.client.ReqFactory;
import hum.client.adapter.JanrainWrapper;
import hum.client.events.MeEvent;
import hum.client.events.MeEventHandler;
import hum.client.model.InfoProxy;

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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;

@Singleton
public class Root extends Composite implements MeEventHandler {

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
    HTMLPanel content;

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

    boolean initialized = false;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));
        bus.addHandler(MeEvent.TYPE, this);
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
        if (meEvent == null || meEvent.user == null) {
            userName.setInnerText(null);
            signOutWrapper.getStyle().setDisplay(Style.Display.NONE);
            avatar.setSrc(null);
            avatar.setAlt(null);
            report.setVisible(true);
            return;
        }
        InfoProxy info = meEvent.user.getInfo().get(0);
        userName.setInnerText("Welcome, " + info.getDisplayName() + "!");
        avatar.setAlt(info.getDisplayName());
        avatar.setSrc(info.getPhoto());
        avatar.setHeight(72);
        report.setVisible(false);
        signOutWrapper.getStyle().setDisplay(Style.Display.INLINE);
    }

    @SuppressWarnings({"UnusedParameters"})
    @UiHandler("signOut")
    void signOut(ClickEvent signOut) {
        reqFactory.userRequest().signOut().fire(new Receiver<Void>() {
            @Override
            public void onSuccess(Void response) {
//                janrainWrapper.signOut();
                dispatch(null);
            }
        });
    }
}
