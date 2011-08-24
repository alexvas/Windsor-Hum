package hum.server.services;

import hum.server.model.Hum;

import java.util.List;

public interface HumService {
    Hum latest();

    Hum save(Hum toSave);

    List<Hum> mine();

    List<Hum> overview();

    List<Hum> yesterday();

    List<Hum> lastWeek();

    Iterable<Hum> all();
}
