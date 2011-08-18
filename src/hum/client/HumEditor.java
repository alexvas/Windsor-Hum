package hum.client;

import hum.client.widget.CommentEditor;
import hum.client.widget.LevelEditor;
import hum.client.widget.StartedEditor;
import hum.client.widget.Summary;

import com.google.gwt.editor.client.Editor;
import com.google.inject.Inject;

public class HumEditor implements Editor<HumEntry> {
    @Inject
    Summary point;

    @Inject
    Summary address;

    @Inject
    LevelEditor level;

    @Inject
    StartedEditor start;

    @Inject
    CommentEditor comment;
}
