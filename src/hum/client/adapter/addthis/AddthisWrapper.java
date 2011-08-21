package hum.client.adapter.addthis;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.Window;

public class AddthisWrapper {

    public void load(String id) {
        ScriptElement script = Document.get().createScriptElement();
        script.setSrc(Window.Location.getProtocol() + "//s7.addthis.com/js/250/addthis_widget.js#domready=1&pubid=" + id);
        script.setType("text/javascript");
        Document.get().getBody().appendChild(script);
    }
}
