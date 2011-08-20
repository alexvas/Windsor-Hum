package hum.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface Resources extends ClientBundle {
    Resources RESOURCES = GWT.create(Resources.class);

    @Source("style.css")
    Style style();
}