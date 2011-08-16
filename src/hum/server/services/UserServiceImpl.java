package hum.server.services;

import hum.server.CurrentUser;
import hum.server.adapter.janrain.json.Profile;
import hum.server.model.User;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class UserServiceImpl extends DAOBase implements UserService {
    private static final Logger log = Logger.getLogger(UserService.class.getName());

    static {
        ObjectifyService.register(User.class);
    }

    @Inject
    private Provider<CurrentUser> currentUser;

    @Override
    public User me() {
        if (currentUser.get().getUserKey() == null) {
            return null;
        }
        try {
            return ofy().get(currentUser.get().getUserKey());
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Override
    public User save(final Profile profile) {
        User user = me();
        if (user == null) {
            user = ofy().query(User.class)
                    .filter("info.identifier", profile.identifier)
                    .filter("info.providerName", profile.providerName)
                    .get();
        }
        if (user == null) {
            user = new User();
            User.Info info = new User.Info();
            updateInfo(info, profile);
            user.getInfo().add(info);
            ofy().put(user);
            return user;
        }
        User.Info info = null;
        for (User.Info i : user.getInfo()) {
            if (profile.identifier.equals(i.identifier) && profile.providerName.equals(i.providerName)) {
                info = i;
                break;
            }
        }
        if (info == null) {
            // possible account addition. We do not support account merge yet
            log.warning("no info for matched user. save aborted");
            return user;
        }
        if (updateInfo(info, profile)) {
            ofy().put(user);
        }
        return user;
    }

    private boolean updateInfo(User.Info in, Profile profile) {
        in.identifier = profile.identifier;
        in.providerName = profile.providerName;
        boolean changed = false;
        if (!equalsNullable(in.getDisplayName(), profile.displayName)) {
            in.setDisplayName(profile.displayName);
            changed = true;
        }
        if (!equalsNullable(in.getPhoto(), profile.photo)) {
            in.setPhoto(profile.photo);
            changed = true;
        }
        return changed;
    }

    boolean equalsNullable(String left, String right) {
        return left == null && right == null || left != null && left.equals(right);
    }
}
