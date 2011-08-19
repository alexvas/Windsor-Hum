package hum.server.services;

import hum.server.CurrentUser;
import hum.server.model.Hum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class HumServiceImpl extends DAOBase implements HumService {
    private static final Logger log = Logger.getLogger(UserService.class.getName());

    static {
        ObjectifyService.register(Hum.class);
    }

    @Inject
    private Provider<Saver<Hum>> saver;

    @Inject
    private CurrentUser currentUser;

    @Override
    public List<Hum> mine() {
        return currentUser.getUserKey() == null
                ? Collections.<Hum>emptyList()
                : toList(query().filter("owner", currentUser.getUserKey()).order("-start"));
    }

    @Override
    public List<Hum> overview() {
        return toList(query().order("-start").limit(100));
    }

    @Override
    public Hum latest() {
        return query().order("-start").get();
    }

    @Override
    public Hum save(Hum hum) {
        if (currentUser.getUserKey() == null) {
            log.warning("unknown user is saving hum");
            return null;
        }
        hum.owner = currentUser.getUserKey();
        return saver.get().save(hum);
    }

    private List<Hum> toList(Query<Hum> query) {
        List<Hum> items = new ArrayList<Hum>();
        for (Hum hum : query.fetch()) {
            items.add(hum);
        }
        return items;
    }

    private Query<Hum> query() {
        return ofy().query(Hum.class);
    }
}
