package hum.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum Utils {
    UTILS;

    public <T> String join(final String delimiter, T... objs) {
        return join(delimiter, Arrays.asList(objs));
    }

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

    public int round(double val) {
        return (int) Math.round(val);
    }

}