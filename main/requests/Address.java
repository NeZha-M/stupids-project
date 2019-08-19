package nl.essent.automation.requests;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class Address {
    private final String ADDRESS_EXTENSIONS_URL = "/accounts/findInstallationAddresses";

    private String getAddressExtensionURL(String post_code, String house_number, String version) {
        String url = ADDRESS_EXTENSIONS_URL + "/v" + version;
        return url + "?postcode=" + post_code + "&house_number=" + house_number;
    }

    public Response getAddressExtensions(String post_code, String house_number, String version) {
        return SerenityRest.given().get(getAddressExtensionURL(post_code, house_number, version));
    }
}
