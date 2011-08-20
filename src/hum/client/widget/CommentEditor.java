package hum.client.widget;

import hum.client.ModeHolder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.ui.client.adapters.ValueBoxEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class CommentEditor extends Composite implements IsEditor<ValueBoxEditor<String>> {

    interface Binder extends UiBinder<Widget, CommentEditor> {
    }

    @Inject
    private ModeHolder modeHolder;

    private static Binder binder = GWT.create(Binder.class);

    private boolean initialized = false;

    @UiField
    TextArea comment;

    @Inject
    private EventBus bus;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));
        comment.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                modeHolder.userEvent();
            }
        });
    }

    @Override
    public ValueBoxEditor<String> asEditor() {
        return comment.asEditor();
    }
}
