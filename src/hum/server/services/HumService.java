package hum.server.services;

import hum.server.model.Hum;

import java.util.List;

public interface HumService {
    List<Hum> mine();

    List<Hum> overview();

    Hum latest();

    Hum save(Hum toSave);
}
