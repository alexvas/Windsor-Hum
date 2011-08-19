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
import hum.client.events.PointEvent;
import hum.client.events.PointEventHandler;
import hum.client.maps.Animation;
import hum.client.maps.MapOptions;
import hum.client.maps.Marker;
import hum.client.maps.MarkerOptions;
import hum.client.maps.geocoder.GeocoderService;
import hum.client.model.HumProxy;
import hum.client.model.PointProxy;

import com.google.gwt.maps.client.Map;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class Mapper implements PointEventHandler, LevelEventHandler, MapsLoadedEventHandler, ModeEventHandler {

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
    boolean currentHumDetached = true;

    private Element mapPlace;
    boolean mapsLoaded = false;

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
            return;
        }
        if (currentHum == null) {
            currentHum = buildMarkerForCurrentHum(point);
        } else {
            currentHum.setPosition(LatLng.newInstance(point.getLat(), point.getLng()));
        }
        if (currentHumDetached) {
            currentHum.setMap(map);
            currentHumDetached = false;
        }
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
                .shadow(levelHelper.icon(level).getShadow())
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
        if (mapsLoaded && !currentHumDetached) {
            currentHum.setIcon(levelHelper.icon(event.level).getIcon());
        } else {
            pendingLevel = event.level;
        }
    }

    @Override
    public void dispatch(MapsLoadedEvent event) {
        mapsLoaded = true;
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
    }
}
