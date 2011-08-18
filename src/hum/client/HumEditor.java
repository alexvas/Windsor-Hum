package hum.client;

import hum.client.model.HumProxy;
import hum.client.widget.CommentEditor;
import hum.client.widget.LevelEditor;
import hum.client.widget.StartedEditor;

import com.google.gwt.editor.client.Editor;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HumEditor implements Editor<HumProxy> {
    @Inject
    PointEditor point;

    @Inject
    AddressEditor address;

    @Inject
    LevelEditor level;

    @Inject
    StartedEditor start;

    @Inject
    CommentEditor comment;
}
