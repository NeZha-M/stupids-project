package nl.essent.automation.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import org.json.JSONObject;

public class Consents {
    private static final String UPDATE_LOYALTY_CONSENT = "/customer/loyaltyconsent";
    private static final String VIEW_LOYALTY_CONSENT = "/customer/loyaltyconsent";

    private String getUpdateLoyaltyConsentURL(String version_number) {
        return UPDATE_LOYALTY_CONSENT + "/v" + version_number;
    }

    private String getViewLoyaltyConsentURL(String version_number, String customer_id) {
        return VIEW_LOYALTY_CONSENT + "/v" + version_number+"?account_id="+customer_id;
    }

    public Response getLoyaltyConsents(String account_id, String versionNumber) {
        return SerenityRest.given().get(getViewLoyaltyConsentURL(versionNumber,account_id));
    }

    private String updateConsentJSON(String customer_id, String loyalty_consent_status, String airmiles_consent_status, int airmiles_card_number) {
        JSONObject update_thuisvoordeel_request = new JSONObject();
        update_thuisvoordeel_request.put("customer_id", customer_id);
        JSONObject loyalty_consent = new JSONObject();
        loyalty_consent.put("consent_status", loyalty_consent_status);
        update_thuisvoordeel_request.put("loyalty_consent", loyalty_consent);
        if (airmiles_card_number != 0 || !(airmiles_consent_status == null)) {
            JSONObject airmiles_consent = new JSONObject();
            if (!(airmiles_consent_status == null)) {
                airmiles_consent.put("consent_status", airmiles_consent_status);
            }
            if (airmiles_card_number != 0)
                airmiles_consent.put("airmiles_cardnumber", airmiles_card_number);
            update_thuisvoordeel_request.put("airmiles_consent", airmiles_consent);
        }
        return update_thuisvoordeel_request.toString();
    }

    public Response updateThuisvoordeelCosnent(String customer_id, String loyalty_consent_status, String airmiles_consent_status, int airmiles_card_number, String version_number, String authorization) {
        return SerenityRest.given().contentType(ContentType.JSON).header("Authorization", authorization).body(updateConsentJSON(customer_id, loyalty_consent_status, airmiles_consent_status, airmiles_card_number)).post(getUpdateLoyaltyConsentURL(version_number));
    }
}
