package hum.server;

import javax.servlet.http.HttpServletRequest;

public enum ServerUtils {
    SERVER_UTILS;

    public static final String PROTOCOL_DELIMETER = "://";
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final char COLON = ':';
    public static final String SLASH = "/";
    public static final String DIES = "#";
    public static final String QUESTION_MARK = "?";

    public String getUrl(HttpServletRequest req, String path, String query, String anchor) {
        String scheme = req.getScheme();
        StringBuilder url = new StringBuilder(scheme)
                .append(PROTOCOL_DELIMETER)
                .append(req.getServerName());
        int port = req.getServerPort();
        if ((scheme.equals(HTTP) && port != 80)
                || (scheme.equals(HTTPS) && port != 443)) {
            url.append(COLON);
            url.append(req.getServerPort());
        }
        if (!empty(path)) {
            if (!path.startsWith(SLASH)) {
                url.append(SLASH);
            }
            url.append(path);
        }
        if (!empty(query)) {
            url.append(QUESTION_MARK).append(query);
        }

        if (!empty(anchor)) {
            url.append(DIES).append(anchor);
        }

        return url.toString();
    }

    public boolean empty(String s) {
        return s == null || s.isEmpty();
    }

}
