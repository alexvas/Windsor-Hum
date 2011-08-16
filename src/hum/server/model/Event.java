package hum.server.model;

import hum.client.model.EventProxy;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import com.googlecode.objectify.Key;

public class Event {
    @Id
    private Long id;

    public Key<User> owner;

    public double lat;
    public double lng;

    public String country;
    public String region;
    public String postcode;

    public String address;

    public EventProxy.Level level;

    public Date start;
    public Date end;

    public String comment;

    private Date created = new Date();

    public Date updated;

    @Version
    private Integer version;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Event");
        sb.append("{id=").append(id);
        sb.append(", user='").append(owner).append('\'');
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

    @PrePersist
    void updateTimestamp() {
        updated = new Date();
    }
}
