package hum.client.model;

import hum.server.model.Hum;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = Hum.Address.class)
public interface AddressProxy extends ValueProxy {
    String getCountry();

    void setCountry(String country);

    String getRegion();

    void setRegion(String region);

    String getPostcode();

    void setPostcode(String postcode);

    String getAddressLine();

    void setAddressLine(String addressLine);

    static class AddressWrapper implements AddressProxy {
        private String country;
        private String region;
        private String postcode;
        private String addressLine;

        @Override
        public String getCountry() {
            return country;
        }

        @Override
        public void setCountry(String country) {
            this.country = country;
        }

        @Override
        public String getRegion() {
            return region;
        }

        @Override
        public void setRegion(String region) {
            this.region = region;
        }

        @Override
        public String getPostcode() {
            return postcode;
        }

        @Override
        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        @Override
        public String getAddressLine() {
            return addressLine;
        }

        @Override
        public void setAddressLine(String addressLine) {
            this.addressLine = addressLine;
        }
    }

}
