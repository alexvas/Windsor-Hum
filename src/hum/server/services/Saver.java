package hum.server.services;

import java.util.logging.Logger;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class Saver<T> {
    private static final Logger log = Logger.getLogger(Saver.class.getName());

    public T save(T t) {
        Objectify ofy = ObjectifyService.beginTransaction();
        try {
            ofy.put(t);
            ofy.getTxn().commit();
        } finally {
            if (ofy.getTxn().isActive()) {
                log.warning("saving " + t + " failed");
                ofy.getTxn().rollback();
            }
        }
        return t;
    }
}
