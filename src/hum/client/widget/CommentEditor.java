package hum.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.ui.client.adapters.ValueBoxEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@SuppressWarnings({"deprecation"})
@Singleton
public class CommentEditor extends Composite implements IsEditor<ValueBoxEditor<String>> {

    interface Binder extends UiBinder<Widget, CommentEditor> {
    }

    private static Binder binder = GWT.create(Binder.class);

    boolean initialized = false;

    @UiField
    TextArea comment;

    @UiField
    Button share;

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public ValueBoxEditor<String> asEditor() {
        return comment.asEditor();
    }
}
