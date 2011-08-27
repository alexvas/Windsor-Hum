package hum.server.services;

import hum.server.model.Hum;

import java.util.Date;
import java.util.List;

public interface HumService {
    Hum latest();

    Hum save(Hum toSave);

    List<Hum> mine();

    List<Hum> overview();

    List<Hum> period(Date from, Date to);

    List<Hum> lastDecade();

    Iterable<Hum> all();
}
