package hum.client.widget;

import hum.client.HumWorkflow;
import hum.client.events.MeEvent;
import hum.client.events.MeEventHandler;
import hum.client.model.UserProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class UserMenu extends Composite implements MeEventHandler {

    interface Binder extends UiBinder<Widget, UserMenu> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Inject
    private EventBus bus;

    private boolean initialized = false;

    @UiField
    MenuItem reportNewHum;

    @UiField
    MenuItem editLastHum;

    @UiField
    MenuItem overview;

    @UiField
    MenuItem yesterday;

    @UiField
    MenuItem lastDecade;

    @Inject
    private HumWorkflow humWorkflow;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));
        setVisible(false);
        reportNewHum.setCommand(new Command() {
            @Override
            public void execute() {
                humWorkflow.reportNewHum();
            }
        });
        editLastHum.setCommand(new Command() {
            @Override
            public void execute() {
                humWorkflow.editLastHum();
            }
        });
        overview.setCommand(new Command() {
            @Override
            public void execute() {
                humWorkflow.showMeLastSubmitted();
            }
        });
        yesterday.setCommand(new Command() {
            @Override
            public void execute() {
                humWorkflow.showMeYesterday();
            }
        });
        lastDecade.setCommand(new Command() {
            @Override
            public void execute() {
                humWorkflow.showMeLastDecade();
            }
        });
        bus.addHandler(MeEvent.TYPE, this);
    }

    @Override
    public void dispatch(MeEvent meEvent) {
        setUser(meEvent.user);
    }

    private void setUser(UserProxy user) {
        setVisible(user != null);
    }
}
