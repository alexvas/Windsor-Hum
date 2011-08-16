package hum.client.widget;

import hum.client.adapter.JanrainWrapper;
import hum.client.events.MeEvent;
import hum.client.events.MeEventHandler;
import hum.client.model.InfoProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class Root extends Composite implements MeEventHandler {

    interface Binder extends UiBinder<Widget, Root> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private JanrainWrapper janrainWrapper;

    @Inject
    private EventBus bus;

    @UiField
    HTML mapPlace;

    @UiField
    HTMLPanel content;

    @UiField
    Anchor report;

    @UiField
    HeadingElement userName;
    @UiField
    ImageElement avatar;

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
    void click(ClickEvent click) {
        String callback = Window.Location
                .createUrlBuilder()
                .setPath(JanrainWrapper.JANRAIN_CALLBACK)
                .buildString();
        janrainWrapper.show(callback, "windsorhum.rpxnow.com");
    }


    @Override
    public void dispatch(MeEvent meEvent) {
        if (meEvent.user != null) {
            InfoProxy info = meEvent.user.getInfo().get(0);
            userName.setInnerText("Welcome, " + info.getDisplayName() + "!");
            avatar.setAlt(info.getDisplayName());
            avatar.setSrc(info.getPhoto());
            avatar.setHeight(72);
            report.setVisible(false);
        }
    }

}
