package hum.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;

public enum ClientUtils {
    CLIENT_UTILS;

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

    public <T> String join(final String delimiter, final Iterable<T> objs) {
        Iterator<T> iter = objs.iterator();
        if (!iter.hasNext()) {
            return "";
        }
        StringBuilder buffer = new StringBuilder(String.valueOf(iter.next()));
        while (iter.hasNext()) {
            T value = iter.next();
            if (value == null) {
                continue;
            }
            String s = String.valueOf(value);
            if (s.isEmpty()) {
                continue;
            }
            buffer.append(delimiter).append(s);
        }
        return buffer.toString();
    }


    public boolean empty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public List<String> filterOutEmpty(List<String> in) {
        List<String> out = new ArrayList<String>();
        for (String s : in) {
            if (!empty(s)) {
                out.add(s.trim());
            }
        }
        return out;
    }

    public boolean same(String left, String right) {
        return (left == null && right == null) || (left != null && left.equals(right));
    }

    public String capitalize(String in) {
        if (empty(in)) {
            return in;
        }
        in = in.trim();
        if (in.length() == 1) {
            return in.toUpperCase();
        }
        return in.substring(0, 1).toUpperCase() + in.substring(1).toLowerCase();
    }

}