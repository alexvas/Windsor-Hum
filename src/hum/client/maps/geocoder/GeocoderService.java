package hum.client.maps.geocoder;

import hum.client.HumWorkflow;
import hum.client.events.AddressEvent;
import hum.client.events.PointEvent;
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

    @Inject
    private HumWorkflow humWorkflow;

    public void direct(String address) {
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
                    onResponse(
                            "OK".equals(status)
                                    ? results.get(0)
                                    : null,
                            true
                    );
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
                    onResponse(
                            "OK".equals(status)
                                    ? results.get(0)
                                    : null,
                            false
                    );
                }
            });
        } catch (Exception e) {
            Log.error("exception while geocoding: ", e);
        }
    }


    private void onResponse(
            GeocoderResult result,
            boolean propagateResolvedLocation
    ) {
        if (propagateResolvedLocation) {
            if (result == null) {
                bus.fireEvent(new PointEvent(null));
            } else {
                PointProxy point = humWorkflow.createPoint();
                LatLng latLng = result.geometry().location();
                point.setLat(latLng.getLatitude());
                point.setLng(latLng.getLongitude());
                bus.fireEvent(new PointEvent(point));
            }
        }

        final AddressProxy address;
        if (result == null) {
            address = null;
        } else {
            address = humWorkflow.createAddress();
            for (int i = 0; i < result.addressComponents().length(); ++i) {
                AddressComponent c = result.addressComponents().get(i);
                for (int j = 0; j < c.types().length(); ++j) {
                    String type = c.types().get(j);
                    if ("country".equals(type)) {
                        address.setCountry(c.shortName());
                    } else if ("postal_code".equals(type)) {
                        address.setPostcode(c.shortName());
                    } else if ("administrative_area_level_1".equals(type)) {
                        address.setRegion(c.shortName());
                    } else if ("route".equals(type)) {
                        address.setAddressLine(c.shortName());
                    }
                }
            }
        }
        bus.fireEvent(new AddressEvent(address));
    }
}
