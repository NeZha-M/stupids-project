package nl.essent.automation.requests;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class Products {
    public static String productDetailsURL = "contracting/contracts";

    public String getProductDetailsURL(String account_id, String version) {
        return productDetailsURL + "/v" + version + "?account_id=" + account_id;
    }

    public Response getProductDetails(String account_id, String version) {
        return SerenityRest.given().get(getProductDetailsURL(account_id, version));
    }
}
