package hum.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class Root extends Composite {

    interface Binder extends UiBinder<Widget, Root> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @UiField
    HTML mapPlace;

    @UiField
    HTML content;

    @UiField
    DivElement errorMessage;

    boolean initialized = false;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));
    }

    public Element getErrorMessage() {
        return errorMessage;
    }

    public com.google.gwt.user.client.Element getMapPlace() {
        init();
        return mapPlace.getElement().cast();
    }
}
