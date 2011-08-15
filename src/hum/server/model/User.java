package hum.server.model;

import siena.Model;
import siena.Query;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

public class User extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String identifier;

    public String providerName;

    public String displayName;

    public String photo;

    @Version
    private Integer version;

    private Date created = new Date();

    public Date updated;

    public static Query<User> all() {
        return Model.all(User.class);
    }

    public static User get(Long id) {
        return Model.getByKey(User.class, id);
    }
}
