package hum.client.maps;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.maps.client.Map;
import com.google.gwt.maps.client.base.LatLng;

public final class MarkerOptions extends JavaScriptObject {
    public static class Builder {
        private final LatLng position;

        private Animation animation;

        private Boolean clickable;

        private String cursor;

        private Boolean draggable;

        private Boolean flat;

        private String iconStr;

        private Icon icon;

        private Map map;

        private Boolean optimized;

        private Boolean raiseOnDrag;

        private String shadowStr;

        private Icon shadow;

        private MarkerShape shape;

        private String title;

        private Boolean visible;

        private Integer zIndex;

        public Builder(LatLng position) {
            this.position = position;
        }

        public Builder animation(Animation animation) {
            this.animation = animation;
            return this;
        }

        public Builder clickable(Boolean clickable) {
            this.clickable = clickable;
            return this;
        }

        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        public Builder draggable(Boolean draggable) {
            this.draggable = draggable;
            return this;
        }

        public Builder flat(Boolean flat) {
            this.flat = flat;
            return this;
        }

        public Builder iconStr(String iconStr) {
            this.iconStr = iconStr;
            return this;
        }

        public Builder icon(Icon icon) {
            this.icon = icon;
            return this;
        }

        public Builder map(Map map) {
            this.map = map;
            return this;
        }

        public Builder optimized(Boolean optimized) {
            this.optimized = optimized;
            return this;
        }

        public Builder raiseOnDrag(Boolean raiseOnDrag) {
            this.raiseOnDrag = raiseOnDrag;
            return this;
        }

        public Builder shadowStr(String shadowStr) {
            this.shadowStr = shadowStr;
            return this;
        }

        public Builder shadow(Icon shadow) {
            this.shadow = shadow;
            return this;
        }

        public Builder shape(MarkerShape shape) {
            this.shape = shape;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder visible(Boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder zIndex(Integer zIndex) {
            this.zIndex = zIndex;
            return this;
        }

        public MarkerOptions build() {
            MarkerOptions opts = newInstance();
            opts.position(position);

            if (animation != null) {
                opts.animation(animation.animation);
            }

            if (clickable != null) {
                opts.clickable(clickable);
            }

            if (cursor != null) {
                opts.cursor(cursor);
            }

            if (draggable != null) {
                opts.draggable(draggable);
            }

            if (flat != null) {
                opts.flat(flat);
            }

            if (iconStr != null) {
                opts.iconStr(iconStr);
            }

            if (icon != null) {
                opts.icon(icon);
            }

            if (map != null) {
                opts.map(map);
            }

            if (optimized != null) {
                opts.optimized(optimized);
            }

            if (raiseOnDrag != null) {
                opts.raiseOnDrag(raiseOnDrag);
            }

            if (shadowStr != null) {
                opts.shadowStr(shadowStr);
            }

            if (shadow != null) {
                opts.shadow(shadow);
            }

            if (shape != null) {
                opts.shape(shape);
            }

            if (title != null) {
                opts.title(title);
            }

            if (visible != null) {
                opts.visible(visible);
            }

            if (zIndex != null) {
                opts.zIndex(zIndex);
            }

            return opts;
        }
    }

    protected MarkerOptions() {
    }

    private native void position(LatLng val) /*-{
        this.position = val
    }-*/;

    private native void animation(int val) /*-{
        this.animation = val
    }-*/;

    private native void clickable(boolean val) /*-{
        this.clickable = val
    }-*/;

    private native void cursor(String val) /*-{
        this.cursor = val
    }-*/;

    private native void draggable(boolean val) /*-{
        this.draggable = val
    }-*/;

    private native void flat(boolean val) /*-{
        this.flat = val
    }-*/;

    private native void iconStr(String val) /*-{
        this.iconStr = val
    }-*/;

    private native void icon(Icon val) /*-{
        this.icon = val
    }-*/;

    private native void map(Map val) /*-{
        this.map = val
    }-*/;

    private native void optimized(boolean val) /*-{
        this.optimized = val
    }-*/;

    private native void raiseOnDrag(boolean val) /*-{
        this.raiseOnDrag = val
    }-*/;

    private native void shadowStr(String val) /*-{
        this.shadowStr = val
    }-*/;

    private native void shadow(Icon val) /*-{
        this.shadow = val
    }-*/;

    private native void shape(MarkerShape val) /*-{
        this.shape = val
    }-*/;

    private native void title(String val) /*-{
        this.title = val
    }-*/;

    private native void visible(boolean val) /*-{
        this.visible = val
    }-*/;

    private native void zIndex(int val) /*-{
        this.zIndex = val
    }-*/;

    private static MarkerOptions newInstance() {
        return JavaScriptObject.createObject().cast();
    }


}
