package hum.server.services;

import hum.server.model.Hum;

import java.util.List;

public interface HumService {
    List<Hum> my();

    List<Hum> all();

    Hum latest();

    Hum save(Hum toSave);
}
