package hum.client.widget;

import hum.client.Back;
import hum.client.HumWorkflow;
import hum.client.LevelHelper;
import hum.client.Mode;
import hum.client.events.LevelEvent;
import hum.client.events.LevelEventHandler;
import hum.client.events.MapsLoadedEvent;
import hum.client.events.MapsLoadedEventHandler;
import hum.client.events.ModeEvent;
import hum.client.events.ModeEventHandler;
import hum.client.events.OverviewEvent;
import hum.client.events.OverviewEventHandler;
import hum.client.events.PointEvent;
import hum.client.events.PointEventHandler;
import hum.client.maps.Animation;
import hum.client.maps.MapOptions;
import hum.client.maps.Marker;
import hum.client.maps.MarkerOptions;
import hum.client.maps.geocoder.GeocoderService;
import hum.client.model.HumProxy;
import hum.client.model.PointProxy;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.maps.client.Map;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class Mapper implements PointEventHandler, LevelEventHandler, MapsLoadedEventHandler,
        ModeEventHandler, OverviewEventHandler {

    private EventBus bus;

    @Inject
    private GeocoderService geocoderService;

    @Inject
    private LevelHelper levelHelper;

    @Inject
    private HumWorkflow humWorkflow;

    private Mode mode;

    protected Map map;
    private PointProxy pendingPoint = null;
    private HumProxy.Level pendingLevel = null;

    private Marker currentHum = null;
    private List<Marker> overview = new ArrayList<Marker>();

    private Element mapPlace;

    private static interface BackLatLng extends Back<LatLng> {
        @Override
        void call(LatLng latLng);
    }

    private final BackLatLng firePositionChange = new BackLatLng() {
        @Override
        public void call(LatLng latLng) {
            switch (mode) {
                case NEW: // fall through
                case LAST:
                    firePositionChange(latLng);
                    break;
                case LIST:
                    break;
                default:
                    throw new RuntimeException("mode not supported: " + mode);
            }
        }
    };

    @Inject
    Mapper(EventBus bus) {
        this.bus = bus;
        bus.addHandler(PointEvent.TYPE, this);
        bus.addHandler(LevelEvent.TYPE, this);
        bus.addHandler(MapsLoadedEvent.TYPE, this);
        bus.addHandler(ModeEvent.TYPE, this);
        bus.addHandler(OverviewEvent.TYPE, this);
    }

    public void initMap(Element mapPlace) {
        this.mapPlace = mapPlace;
    }

    private void firePositionChange(LatLng latLng) {
        geocoderService.reverse(latLng);
        PointProxy point = humWorkflow.createPoint();
        point.setLat(latLng.getLatitude());
        point.setLng(latLng.getLongitude());
        bus.fireEvent(new PointEvent(point));
    }

    private native void addClickListener(Map map, Back<LatLng> back) /*-{
        $wnd.google.maps.event.addListener(map, 'click', function(e) {
            back.@hum.client.widget.Mapper.BackLatLng::call(Lcom/google/gwt/maps/client/base/LatLng;)(e.latLng);
        });
    }-*/;

    private native void addDragendListener(Marker marker, Back<LatLng> back) /*-{
        $wnd.google.maps.event.addListener(marker, 'dragend', function(e) {
            back.@hum.client.widget.Mapper.BackLatLng::call(Lcom/google/gwt/maps/client/base/LatLng;)(e.latLng);
        });
    }-*/;

    @Override
    public void dispatch(PointEvent event) {
        if (map == null) {
            pendingPoint = event.point;
            return;
        }
        putPin(event.point);
    }

    private void putPin(PointProxy point) {
        if (point == null) {
            detachCurrentHum();
            return;
        }
        if (currentHum == null) {
            currentHum = buildMarkerForCurrentHum(point);
        } else {
            currentHum.setPosition(LatLng.newInstance(point.getLat(), point.getLng()));
        }
        attachCurrentHum();
    }

    private void detachCurrentHum() {
        if (currentHum == null || currentHum.getMap() == null) {
            return;
        }
        currentHum.setMap(null);
    }

    private void attachCurrentHum() {
        if (currentHum == null || currentHum.getMap() != null) {
            return;
        }
        currentHum.setMap(map);
    }

    private Marker buildMarkerForCurrentHum(PointProxy point) {
        final HumProxy.Level level;
        if (pendingLevel == null) {
            level = HumProxy.Level.HIGH;
        } else {
            level = pendingLevel;
            pendingLevel = null;
        }
        MarkerOptions opts = new MarkerOptions.Builder(LatLng.newInstance(point.getLat(), point.getLng()))
                .icon(levelHelper.icon(level).getIcon())
                .shadow(levelHelper.icon(HumProxy.Level.HIGH).getShadow())
//                .shape(red.getShape())
                .animation(Animation.DROP)
                .draggable(true)
                .build();
        Marker marker = Marker.newInstance(opts);
        addDragendListener(marker, firePositionChange);
        return marker;
    }

    @Override
    public void dispatch(LevelEvent event) {
        HumProxy.Level level = event.level;
        if (map != null && currentHum != null) {
            currentHum.setIcon(levelHelper.icon(
                    level == null
                            ? HumProxy.Level.HIGH
                            : level
            ).getIcon());
        } else {
            pendingLevel = level;
        }
    }

    @Override
    public void dispatch(MapsLoadedEvent event) {
        MapOptions mapOptions = MapOptions.newInstance();
        mapOptions.center(LatLng.newInstance(42.163403, -82.900772));
        mapOptions.zoom(11);
        mapOptions.roadmap();
        map = Map.newInstance(mapPlace, mapOptions);

        addClickListener(map, firePositionChange);
        if (pendingPoint != null) {
            putPin(pendingPoint);
            pendingPoint = null;
        }
    }

    @Override
    public void dispatch(ModeEvent event) {
        mode = event.mode;
        switch (mode) {
            case NEW:
                detachOverview();
                break;
            case LAST:
                detachOverview();
                attachCurrentHum();
                break;
            case LIST:
                detachCurrentHum();
                attachOverview();
                break;
            default:
                throw new RuntimeException("mode not supported: " + mode);
        }
    }

    @Override
    public void dispatch(OverviewEvent event) {
        detachOverview();
        overview.clear();

        for (HumProxy hum : event.hums) {
            PointProxy point = hum.getPoint();
            HumProxy.Level level = hum.getLevel();

            if (point == null || level == null) {
                continue;
            }

            MarkerOptions opts = new MarkerOptions.Builder(LatLng.newInstance(point.getLat(), point.getLng()))
                    .icon(levelHelper.icon(level).getIcon())
                    .shadow(levelHelper.icon(HumProxy.Level.HIGH).getShadow())
                    .animation(Animation.DROP)
                    .draggable(false)
                    .clickable(false)
                    .build();
            Marker marker = Marker.newInstance(opts);
            overview.add(marker);
        }

        bus.fireEvent(new ModeEvent(Mode.LIST));
    }

    private void attachOverview() {
        if (map == null) {
            return;
        }
        for (Marker m : overview) {
            m.setMap(map);
        }
    }

    private void detachOverview() {
        for (Marker m : overview) {
            m.setMap(null);
        }
    }

}
