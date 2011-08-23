package hum.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
    Resources RESOURCES = GWT.create(Resources.class);

    @Source("style.css")
    Style style();

    @Source("images/shadow.png")
    ImageResource shadow();

    @Source("images/pin_low.png")
    ImageResource pinLow();

    @Source("images/pin_medium.png")
    ImageResource pinMedium();

    @Source("images/pin_high.png")
    ImageResource pinHigh();

    @Source("images/btn_left.png")
    ImageResource buttonLeft();

    @Source("images/btn_right.png")
    ImageResource buttonRight();

    @Source("images/btn_stretch.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.Horizontal)
    ImageResource buttonStretch();
}