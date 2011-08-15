package hum.server.services;

import hum.server.CurrentUser;
import hum.server.model.Event;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;

public class EventServiceImpl implements EventService {

    @Inject
    private CurrentUser currentUser;

    @Override
    public List<Event> my() {
        return currentUser.userId == null
                ? Collections.<Event>emptyList()
                : Event.all().filter("user", currentUser.userId).order("-start").fetch();
    }

    @Override
    public List<Event> all() {
        return Event.all().order("-start").fetch();
    }

    @Override
    public Event latest() {
        return Event.all().order("-start").get();
    }

    @Override
    public void save(Event event) {
        event.updated = new Date();
        event.save();
    }
}
