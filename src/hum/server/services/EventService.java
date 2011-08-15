package hum.server.services;

import hum.server.model.Event;

import java.util.List;

public interface EventService {
    List<Event> my();

    List<Event> all();

    Event latest();

    void save(Event toSave);
}
