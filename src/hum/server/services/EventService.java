package hum.server.services;

import hum.server.model.Event;

public interface EventService {
    Iterable<Event> my();

    Iterable<Event> all();

    Event latest();

    void save(Event toSave);
}
