package hum.server.services;

import hum.server.CurrentUser;
import hum.server.model.Hum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
    public Hum latest() {
        return query().filter("owner", currentUser.getUserKey()).order("-start").get();
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
    public List<Hum> period(Date from, Date to) {
        Query<Hum> query = query();
        if (from != null) {
            query.filter("start > ", from);
        }
        if (to != null) {
            query.filter("start <= ", to);
        }
        return toList(query.order("-start"));
    }

    @Override
    public List<Hum> lastDecade() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -10);
        return toList(query().filter("start > ", cal.getTime()).order("-start"));
    }

    @Override
    public Iterable<Hum> all() {
        return query().order("-start");
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
