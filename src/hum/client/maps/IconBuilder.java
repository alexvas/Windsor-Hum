package hum.client.maps;

import com.google.gwt.maps.client.base.Size;

public class IconBuilder {
    private static final String BASE_URL = "http://chart.apis.google.com/chart?cht=mm";
    private static final String SHADOW_URL = "/images/shadow.png";

    private int width = 32;
    private int height = 32;
    private String primaryColor = "ff0000";
    private String strokeColor = "000000";
    private String cornerColor = "ffffff";

    public IconBuilder width(int width) {
        this.width = width;
        return this;
    }

    public IconBuilder height(int height) {
        this.height = height;
        return this;
    }

    public IconBuilder primaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
        return this;
    }

    public IconBuilder strokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public IconBuilder cornerColor(String cornerColor) {
        this.cornerColor = cornerColor;
        return this;
    }

    MarkerImage cachedIcon = null;

    public MarkerImage getIcon() {
        if (cachedIcon != null) {
            return cachedIcon;
        }
        String iconBase = BASE_URL + "&chs=" + width + "x" + height + "&chco="
                + cornerColor + "," + primaryColor + "," + strokeColor;
        String iconUrl = iconBase + "&ext=.png";
        return cachedIcon = new MarkerImage.Builder(iconUrl)
                .size(Size.newInstance(width, height))
                .anchor(Point.newInstance(width / 2, height))
                .build();
    }

    MarkerImage cachedShadow = null;

    public MarkerImage getShadow() {
        if (cachedShadow != null) {
            return cachedShadow;
        }
        MarkerImage.Builder shadowBuilder = new MarkerImage.Builder(SHADOW_URL)
                .size(Size.newInstance(40, 37))
                .anchor(Point.newInstance(13, 37));
/*

        if (Math.abs(width / 32.0 - 1.0) > 0.05) {
            shadowBuilder.scaledSize(Size.newInstance(
                    CLIENT_UTILS.round(40 * width / 20.0),
                    CLIENT_UTILS.round(37 * width / 20.0))
            );
        }
*/

        return cachedShadow = shadowBuilder.build();
    }

    public MarkerShape getShape() {
        return new MarkerShape.Builder()
                .setCoords(
                        new int[]{
                                width / 2, height,
                                7 * width / 16, 5 * height / 8,
                                5 * width / 16, 7 * height / 16,
                                7 * width / 32, 5 * height / 16,
                                5 * width / 16, height / 8,
                                width / 2, 0,
                                11 * width / 16, height / 8,
                                25 * width / 32, 5 * height / 16,
                                11 * width / 16, 7 * height / 16,
                                9 * width / 16, 5 * height / 8
                        }
                )
                .setType(MarkerShape.Type.poly)
                .build();
    }
}
