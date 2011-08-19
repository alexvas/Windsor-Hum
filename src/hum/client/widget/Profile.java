package hum.client.widget;

import hum.client.Mode;
import hum.client.ReqFactory;
import hum.client.adapter.JanrainWrapper;
import hum.client.events.MeEvent;
import hum.client.events.MeEventHandler;
import hum.client.events.ModeEvent;
import hum.client.model.InfoProxy;
import hum.client.model.UserProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;

@Singleton
public class Profile extends Composite implements MeEventHandler {

    interface Binder extends UiBinder<Widget, Profile> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private JanrainWrapper janrainWrapper;

    @Inject
    private EventBus bus;

    @Inject
    private ReqFactory reqFactory;

    @UiField
    HeadingElement userName;

    @UiField
    ImageElement avatar;

    @UiField
    SpanElement signOutWrapper;

    @UiField
    Anchor signOut;

    private boolean initialized = false;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));
        setVisible(false);
        bus.addHandler(MeEvent.TYPE, this);
    }

    @Override
    public void dispatch(MeEvent meEvent) {
        setUser(meEvent.user);
    }

    private void setUser(UserProxy user) {
        boolean hasUser = user != null;
        if (hasUser) {
            InfoProxy info = user.getInfo().get(0);
            userName.setInnerText("Welcome, " + info.getDisplayName() + "!");
            avatar.setSrc(info.getPhoto());
        }
        setVisible(hasUser);
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
