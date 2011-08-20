package hum.client;

import hum.client.maps.IconBuilder;
import hum.client.model.HumProxy;

import com.google.inject.Singleton;

@Singleton
public class LevelHelper {

    public String color(HumProxy.Level level) {
        switch (level) {
            case LOW:
                return "ffff00";
            case MEDIUM:
                return "00ff00";
            case HIGH:
                return "ff0000";
            default:
                throw new RuntimeException("level " + level + " is not supported");
        }
    }

    private final IconBuilder LOW = new IconBuilder().primaryColor(color(HumProxy.Level.LOW));
    private final IconBuilder MEDIUM = new IconBuilder().primaryColor(color(HumProxy.Level.MEDIUM));
    private final IconBuilder HIGH = new IconBuilder().primaryColor(color(HumProxy.Level.HIGH));

    public IconBuilder icon(HumProxy.Level level) {
        switch (level) {
            case LOW:
                return LOW;
            case MEDIUM:
                return MEDIUM;
            case HIGH:
                return HIGH;
            default:
                throw new RuntimeException("level " + level + " is not supported");
        }
    }



}
