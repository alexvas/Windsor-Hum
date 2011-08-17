package hum.client.maps.geocoder;

import hum.client.events.AddressEvent;
import hum.client.events.PositionEvent;
import hum.client.model.AddressProxy;
import hum.client.model.PointProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.base.LatLng;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class GeocoderService {

    private Geocoder geocoder;

    @Inject
    private EventBus bus;

    public void direct(String address, final boolean propagateResolvedAddress) {
        Log.debug("geocoding address '" + address + "', country 'ca'");
        if (geocoder == null) {
            geocoder = Geocoder.newInstance();
            Log.debug("new Geocoder created");
        }

        GeocoderRequest request = GeocoderRequest.newInstance();
        request.address(address);
        request.region("ca");

        try {
            geocoder.geocode(request, new GeocoderCallback() {
                @Override
                public void back(JsArray<GeocoderResult> results, String status) {
                    Log.debug("geocoder status: " + status);
                    onResponse(results.get(0), true, propagateResolvedAddress);
                }
            });
        } catch (Exception e) {
            Log.error("exception while geocoding: ", e);
        }
    }


    public void reverse(LatLng point) {
        Log.debug("geocoding location " + point);
        if (geocoder == null) {
            geocoder = Geocoder.newInstance();
            Log.debug("new Geocoder created");
        }

        GeocoderRequest request = GeocoderRequest.newInstance();
        request.location(point);

        try {
            geocoder.geocode(request, new GeocoderCallback() {
                @Override
                public void back(JsArray<GeocoderResult> results, String status) {
                    Log.debug("geocoder status: " + status);
                    onResponse(results.get(0), false, true);
                }
            });
        } catch (Exception e) {
            Log.error("exception while geocoding: ", e);
        }
    }


    private void onResponse(
            GeocoderResult result,
            boolean propagateResolvedLocation,
            boolean propagateResolvedAddress
    ) {
        if (propagateResolvedLocation) {
            LatLng point = result.geometry().location();
            bus.fireEvent(new PositionEvent(PointProxy.LatLngWrapper.from(point)));
        }

        if (!propagateResolvedAddress) {
            return;
        }

        AddressProxy.AddressWrapper address = new AddressProxy.AddressWrapper();
        for (int i = 0; i < result.addressComponents().length(); ++i) {
            AddressComponent c = result.addressComponents().get(i);
            for (int j = 0; j < c.types().length(); ++j) {
                String type = c.types().get(j);
                if ("country".equals(type)) {
                    address.setCountry(c.shortName().toLowerCase());
                } else if ("postal_code".equals(type)) {
                    address.setPostcode(c.shortName().toLowerCase());
                } else if ("administrative_area_level_1".equals(type)) {
                    address.setRegion(c.shortName().toLowerCase());
                } else if ("street_address".equals(type)) {
                    address.setAddressLine(c.shortName().toLowerCase());
                }
            }
        }
        bus.fireEvent(new AddressEvent(address));
    }
}
