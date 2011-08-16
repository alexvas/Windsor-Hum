package hum.server.services;

import hum.server.model.Hum;

import java.util.List;

public interface HumService {
    List<Hum> my();

    List<Hum> all();

    Hum latest();

    void save(Hum toSave);
}
