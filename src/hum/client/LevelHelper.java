package hum.client;

import hum.client.maps.IconBuilder;
import hum.client.model.HumProxy;

import com.google.inject.Singleton;

@Singleton
public class LevelHelper {
    private final IconBuilder yellow = new IconBuilder().primaryColor("ffff00");
    private final IconBuilder green = new IconBuilder().primaryColor("00ff00");
    private final IconBuilder red = new IconBuilder().primaryColor("ff0000");

    public IconBuilder icon(HumProxy.Level level) {
        switch (level) {
            case LOW:
                return yellow;
            case MEDIUM:
                return green;
            case HIGH:
                return red;
            default:
                throw new RuntimeException("level " + level + " is not supported");
        }
    }
}
