package nl.essent.automation.requests;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class Account {
    public static String accountsURL = "/accounts/account_id";

    public String getAccountsRequestURL(String account_id, String version) {
        return accountsURL.replace("account_id", account_id) + "/v" + version;
    }

    public Response getAccount(String account_id, String version) {
        return SerenityRest.given().get(getAccountsRequestURL(account_id, version));
    }
}
