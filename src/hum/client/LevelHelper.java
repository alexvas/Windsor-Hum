package hum.client;

import hum.client.maps.IconBuilder;
import hum.client.maps.MarkerImage;
import hum.client.model.HumProxy;
import static hum.client.resources.Resources.RESOURCES;

import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Singleton;

@Singleton
public class LevelHelper {

    public String style(HumProxy.Level level) {
        switch (level) {
            case LOW:
                return RESOURCES.style().pinLow();
            case MEDIUM:
                return RESOURCES.style().pinMedium();
            case HIGH:
                return RESOURCES.style().pinHigh();
            default:
                throw new RuntimeException("level " + level + " is not supported");
        }
    }

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

    public ImageResource image(HumProxy.Level level) {
        switch (level) {
            case LOW:
                return RESOURCES.pinLow();
            case MEDIUM:
                return RESOURCES.pinMedium();
            case HIGH:
                return RESOURCES.pinHigh();
            default:
                throw new RuntimeException("level " + level + " is not supported");
        }
    }


    private MarkerImage LOW;
    private MarkerImage MEDIUM;
    private MarkerImage HIGH;

    public MarkerImage icon(HumProxy.Level level) {
        switch (level) {
            case LOW:
                return LOW == null
                        ? LOW = new IconBuilder().getIcon(image(HumProxy.Level.LOW))
                        : LOW;
            case MEDIUM:
                return MEDIUM == null
                        ? MEDIUM = new IconBuilder().getIcon(image(HumProxy.Level.MEDIUM))
                        : MEDIUM;
            case HIGH:
                return HIGH == null
                        ? HIGH = new IconBuilder().getIcon(image(HumProxy.Level.HIGH))
                        : HIGH;
            default:
                throw new RuntimeException("level " + level + " is not supported");
        }
    }


}
