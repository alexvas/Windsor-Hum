package hum.client;

import hum.client.events.ModeEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class ModeHolder {
    @Inject
    private EventBus bus;

    private Mode mode = Mode.LIST;

    public Mode mode() {
        return mode;
    }

    public void newHumCreated() {
        if (mode != Mode.NEW) {
            mode = Mode.NEW;
            fire();
        }
    }

    public void lastHumLoaded() {
        if (mode != Mode.LAST) {
            mode = Mode.LAST;
            fire();
        }
    }

    public void showList() {
        if (mode != Mode.LIST) {
            mode = Mode.LIST;
            fire();
        }
    }

    public void userEvent() {
        switch (mode) {
            case NEW:
                // do nothing
                break;
            case LAST:
                mode = Mode.UPDATED;
                fire();
                break;
            case UPDATED:
                // do nothing
                break;
            case LIST:
                // do nothing
                break;
            default:
                throw new RuntimeException("mode not supported: " + mode);
        }
    }

    private void fire() {
        bus.fireEvent(new ModeEvent());
    }
}
