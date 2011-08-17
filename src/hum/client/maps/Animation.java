package hum.client.maps;

@SuppressWarnings({"GwtInconsistentSerializableClass"})
public enum Animation {
    BOUNCE(bounce()), DROP(drop());

    public final int animation;

    Animation(int animation) {
        this.animation = animation;
    }

    private static native int drop() /*-{
        return $wnd.google.maps.Animation.DROP;
    }-*/;

    private static native int bounce() /*-{
        return $wnd.google.maps.Animation.BOUNCE;
    }-*/;

}
