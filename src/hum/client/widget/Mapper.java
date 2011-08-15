package hum.client.widget;

import hum.client.maps.MapOptions;

import com.google.gwt.maps.client.Map;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.user.client.Element;
import com.google.inject.Singleton;

@Singleton
public class Mapper {
    private static final String HOME_ICON_URL = "https://chart.googleapis.com/chart?chst=d_map_xpin_icon&chld=pin_star|home|FF0000|FFFF00";
    protected static final String ICON_SHADOW_URL = "http://www.google.com/chart?chst=d_map_pin_shadow";
    private static final String RED_PIN_URL = "https://chart.googleapis.com/chart?chst=d_map_xpin_letter&chld=pin|+|FF0000|000000|FF0000";

    protected Map map;

    public void initMap(Element mapPlace) {
        MapOptions mapOptions = MapOptions.newInstance();
        mapOptions.center(LatLng.newInstance(42.163403, -82.900772));
        mapOptions.zoom(11);
        mapOptions.roadmap();
        map = Map.newInstance(mapPlace, mapOptions);
    }

}
