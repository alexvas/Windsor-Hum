package hum.client.maps;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.maps.client.base.Point;
import com.google.gwt.maps.client.base.Size;

public final class Icon extends JavaScriptObject {
    protected Icon() {
    }

    public static class Builder {
        final private String url;
        private Size size = null;
        private Size scaledSize = null;
        private Point origin = null;
        private Point anchor = null;

        public Builder(String url) {
            this.url = url;
        }

        public Builder size(Size size) {
            this.size = size;
            return this;
        }

        public Builder scaledSize(Size size) {
            this.scaledSize = size;
            return this;
        }

        public Builder origin(Point origin) {
            this.origin = origin;
            return this;
        }

        public Builder anchor(Point anchor) {
            this.anchor = anchor;
            return this;
        }

        public Icon build() {
            return Icon.newInstance(url, size, origin, anchor, scaledSize);
        }
    }

    private static native Icon newInstance(String url, Size size, Point origin, Point anchor, Size scaledSize) /*-{
        return new $wnd.google.maps.MarkerImage(url, size, origin, anchor, scaledSize);
    }-*/;

}
