package hum.server.model;

import hum.client.model.HumProxy;

import java.util.Date;

import com.googlecode.objectify.Key;

public class Hum extends DataObject{
    public Key<User> owner;

    private double lat;
    private double lng;

    private String country;
    private String region;
    private String postcode;

    private String address;

    private HumProxy.Level level;

    private Date start;
    private Date end;

    private String comment;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Hum");
        sb.append("{id=").append(getId());
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

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getAddress() {
        return address;
    }

    public HumProxy.Level getLevel() {
        return level;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getComment() {
        return comment;
    }
}
