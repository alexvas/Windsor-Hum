package hum.client.widget;

import hum.client.Back;
import hum.client.events.PositionEvent;
import hum.client.events.PositionEventHandler;
import hum.client.maps.Animation;
import hum.client.maps.IconBuilder;
import hum.client.maps.MapOptions;
import hum.client.maps.Marker;
import hum.client.maps.MarkerOptions;
import hum.client.model.PointProxy;

import com.google.gwt.maps.client.Map;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class Mapper implements PositionEventHandler {
    public enum Mode {EDIT, LIST}

    public Mode mode = Mode.EDIT;

    @Inject
    private EventBus bus;

    private final IconBuilder yellow = new IconBuilder().primaryColor("00ffff");
    private final IconBuilder green = new IconBuilder().primaryColor("00ff00");
    private final IconBuilder red = new IconBuilder().primaryColor("ff0000");

    protected Map map;
    private PointProxy pendingPoint = null;

    private Marker currentHum = null;
    boolean currentHumDetached = true;

    private static interface BackLatLng extends Back<LatLng> {
        @Override
        void call(LatLng latLng);
    }

    private final BackLatLng firePositionChange = new BackLatLng() {
        @Override
        public void call(LatLng latLng) {
            if (mode != Mode.EDIT || currentHum != null) {
                return;
            }
            firePositionChange(latLng);
        }
    };

    public void initMap(Element mapPlace) {
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
        bus.addHandler(PositionEvent.TYPE, this);
    }

    private void firePositionChange(LatLng latLng) {
        bus.fireEvent(new PositionEvent(ImplPoint.from(latLng)));
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
    public void dispatch(PositionEvent event) {
        if (map == null) {
            pendingPoint = event.point;
            return;
        }
        putPin(event.point);
    }

    private void putPin(PointProxy point) {
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
        MarkerOptions opts = new MarkerOptions.Builder(LatLng.newInstance(point.getLat(), point.getLng()))
                .icon(red.getIcon())
                .shadow(red.getShadow())
//                .shape(red.getShape())
                .animation(Animation.DROP)
                .draggable(true)
//                .flat(false)
//                .visible(true)
                .build();
        Marker marker = Marker.newInstance(opts);
        addDragendListener(marker, firePositionChange);
        return marker;
    }

    private static class ImplPoint implements PointProxy {
        private double lat;
        private double lng;

        @Override
        public double getLat() {
            return lat;
        }

        @Override
        public void setLat(double lat) {
            this.lat = lat;
        }

        @Override
        public double getLng() {
            return lng;
        }

        @Override
        public void setLng(double lng) {
            this.lng = lng;
        }

        private static PointProxy from(LatLng latLng) {
            ImplPoint point = new ImplPoint();
            point.setLat(latLng.getLatitude());
            point.setLng(latLng.getLongitude());
            return point;
        }
    }


}
