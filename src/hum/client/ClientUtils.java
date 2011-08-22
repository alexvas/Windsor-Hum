package hum.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.NumberFormat;

public enum ClientUtils {
    CLIENT_UTILS;

    public final NumberFormat coordFormat = NumberFormat.getFormat("###.######");

    public native String dump(JavaScriptObject obj, String name, String indent, int depth) /*-{
        if (depth > 100) {
            return indent + name + ": <Maximum Depth Reached>\n";
        }

        if (typeof obj == "object") {

            var child = null;
            var output = indent + name + "\n";
            indent += "\t";
            for (var item in obj) {
                try {
                    child = obj[item];
                } catch (e) {
                    child = "<Unable to Evaluate>";
                }

                if (typeof child == "object") {
                    output += this.@hum.client.ClientUtils::dump(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Ljava/lang/String;I)(child, item, indent, depth + 1);
                } else {
                    output += indent + item + ": " + child + "\n";
                }

            }
            return output;
        } else {
            return obj;
        }

    }-*/;
}