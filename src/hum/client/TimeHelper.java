package hum.client;

import java.util.Date;

public enum TimeHelper {
    TIME_HELPER;

    @SuppressWarnings({"deprecation"})
    private final long OFFSET = new Date().getTimezoneOffset() * 60L * 1000L;

    public Date toGmt(Date local) {
        if (local == null) {
            return null;
        }
        return new Date(local.getTime() - OFFSET);
    }

    public Date fromGmt(Date utc) {
        if (utc == null) {
            return null;
        }
        return new Date(utc.getTime() + OFFSET);
    }

}
