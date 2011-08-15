package hum.server.services;

import hum.server.adapter.janrain.json.Profile;
import hum.server.model.User;

public interface UserService {
    User me();

    User save(Profile profile);
}
