package nl.essent.automation.requests;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class ContactLogs {

    private static final String CONTACT_LOGS_URL = "/accounts/account_id/contactLogs";
    private static final String CONTACT_LOG_DETAILS_URL = "/accounts/account_id/contactLogs/contact_log_id/details";

    private String getContactLogRequestURL(String account_id, String versionNumber) {
        return CONTACT_LOGS_URL.replaceAll("account_id", account_id) + "/v" + versionNumber;
    }

    private String getContactLogDetailsURL(String account_id, String contact_log_id, String versionNumber) {
        String contact_log_details_url = CONTACT_LOG_DETAILS_URL.replaceAll("account_id", account_id);
        contact_log_details_url = contact_log_details_url.replaceAll("contact_log_id", contact_log_id);
        return contact_log_details_url + "/v" + versionNumber;
    }

    public Response getContactLogs(String account_id, String versionNumber) {
        return SerenityRest.given().get(getContactLogRequestURL(account_id, versionNumber));
    }

    public Response getContactLogDetails(String account_id, String contact_log_id, String versionNumber) {
        return SerenityRest.given().get(getContactLogDetailsURL(account_id, contact_log_id, versionNumber));
    }
}
