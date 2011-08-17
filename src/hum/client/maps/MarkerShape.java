package hum.client.maps;

import com.google.gwt.core.client.JavaScriptObject;

public final class MarkerShape extends JavaScriptObject {
    public enum Type {circle, poly, rect}

    public static class Builder {
        private int coords[];
        private Type type;

        public Builder setCoords(int[] coords) {
            this.coords = coords;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public MarkerShape build() {
            switch (type) {
                case circle:
                    if (coords.length != 3) {
                        throw new RuntimeException("circle shape must have 3 coords");
                    }
                    break;
                case poly:
                    if (coords.length % 2 != 0) {
                        throw new RuntimeException("circle shape coords number must be odd");
                    }
                    break;
                case rect:
                    if (coords.length != 4) {
                        throw new RuntimeException("rect shape must have 4 coords");
                    }
                    break;
                default:
                    throw new RuntimeException("type not supported: " + type);
            }
            return newInstance(coords, type);
        }
    }

    private static MarkerShape newInstance(int coords[], Type type) {
        MarkerShape shape = JavaScriptObject.createObject().cast();
        shape.coords(coords);
        shape.type(type.name());
        return shape;
    }

    private native void coords(int c[]) /*-{
        this.coords = c;
    }-*/;

    private native void type(String t) /*-{
        this.type = t;
    }-*/;

    protected MarkerShape() {
    }
}
