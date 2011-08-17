package hum.server.model;

import hum.client.model.HumProxy;

import java.util.Date;

import javax.persistence.Embedded;

import com.googlecode.objectify.Key;

public class Hum extends DataObject{
    public Key<User> owner;

    @Embedded
    private Point point = new Point();

    @Embedded
    private Address address = new Address();

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
        sb.append(", point=").append(point);
        sb.append(", address=").append(address);
        sb.append(", start=").append(start);
        sb.append(", level='").append(level).append('\'');
        sb.append(", created=").append(created);
        sb.append('}');
        return sb.toString();
    }

    public Point getPoint() {
        return point;
    }

    public Address getAddress() {
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

    public static class Point {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Point");
            sb.append("{lat=").append(lat);
            sb.append(", lng=").append(lng);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Address {
        private String country;
        private String region;
        private String postcode;
        private String addressLine;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getAddressLine() {
            return addressLine;
        }

        public void setAddressLine(String addressLine) {
            this.addressLine = addressLine;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Address");
            sb.append("{country='").append(country).append('\'');
            sb.append(", region='").append(region).append('\'');
            sb.append(", postcode='").append(postcode).append('\'');
            sb.append(", addressLine='").append(addressLine).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}
