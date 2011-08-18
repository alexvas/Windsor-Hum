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
}
