package hum.server.services;

import hum.server.CurrentUser;
import hum.server.adapter.janrain.json.Profile;
import hum.server.model.User;

import java.util.Date;

import com.google.inject.Inject;

public class UserServiceImpl implements UserService {

    @Inject
    private CurrentUser currentUser;

    @Override
    public User me() {
        return currentUser.userId == null
                ? null
                : User.get(currentUser.userId);
    }

    @Override
    public User save(final Profile profile) {
        User user = me();
        if (user == null) {
            user = User.all().filter("identifier", profile.identifier).get();
        }
        if (user == null) {
            user = new User();
            user.identifier = profile.identifier;
        }
        user.displayName = profile.displayName;
        user.photo = profile.photo;
        user.providerName = profile.providerName;
        user.updated = new Date();
        user.save();
        return user;
    }
}
