package hum.client;

import hum.client.events.PointEvent;
import hum.client.events.PointEventHandler;
import hum.client.model.PointProxy;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class PointEditor implements LeafValueEditor<PointProxy>, PointEventHandler {
    private final EventBus bus;

    private PointProxy currentPoint;

    @Inject
    public PointEditor(EventBus bus) {
        this.bus = bus;
        bus.addHandler(PointEvent.TYPE, this);
    }

    @Override
    public void dispatch(PointEvent event) {
        setPoint(event.point);
    }

    private void setPoint(PointProxy point) {
        currentPoint = point;
    }

    @Override
    public void setValue(PointProxy value) {
        currentPoint = value;
        bus.fireEvent(new PointEvent(currentPoint));
    }

    @Override
    public PointProxy getValue() {
        return currentPoint;
    }
}
