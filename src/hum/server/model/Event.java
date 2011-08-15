package hum.server.model;

import siena.Model;
import siena.Query;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Version;

public class Event extends Model {
    @Id
    private Long id;

    public Long user;

    public double lat;
    public double lng;

    public String country;
    public String region;
    public String postcode;

    public String address;

    public String level;

    public Date start;
    public Date end;

    public String comment;

    private Date created = new Date();

    public Date updated;

    @Version
    private Integer version;

    public static Query<Event> all() {
        return Model.all(Event.class);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Event");
        sb.append("{id=").append(id);
        sb.append(", user='").append(user).append('\'');
        sb.append(", lat=").append(lat);
        sb.append(", lng=").append(lng);
        sb.append(", postcode='").append(postcode).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", start=").append(start);
        sb.append(", level='").append(level).append('\'');
        sb.append(", created=").append(created);
        sb.append('}');
        return sb.toString();
    }
}
