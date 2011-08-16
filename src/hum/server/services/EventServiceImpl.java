package hum.server.services;

import hum.server.CurrentUser;
import hum.server.model.Event;

import java.util.Collections;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class EventServiceImpl extends DAOBase implements EventService {
    private static final Logger log = Logger.getLogger(UserService.class.getName());

    static {
        ObjectifyService.register(Event.class);
    }

    @Inject
    private Provider<Saver<Event>> saver;

    @Inject
    private CurrentUser currentUser;

    @Override
    public Iterable<Event> my() {
        return currentUser.getUserKey() == null
                ? Collections.<Event>emptyList()
                : ObjectifyService.begin().query(Event.class).filter("owner", currentUser.getUserKey()).order("-start").fetch();
    }

    @Override
    public Iterable<Event> all() {
        return ObjectifyService.begin().query(Event.class).order("-start").fetch();
    }

    @Override
    public Event latest() {
        return ObjectifyService.begin().query(Event.class).order("-start").get();
    }

    @Override
    public void save(Event event) {
        if (currentUser.getUserKey() == null) {
            log.warning("unknown user is saving event");
            return;
        }
        event.owner = currentUser.getUserKey();
        saver.get().save(event);
    }
}
