package nl.essent.automation.requests;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class AccountBalance {
    public static String accountBalanceURL = "/accounts/account_id/accountBalance";

    public String getAccountBalanceRequestURL(String account_id) {
        return accountBalanceURL.replace("account_id", account_id);
    }

    public Response getAccountBalance(String account_id) {
        return SerenityRest.given().get(getAccountBalanceRequestURL(account_id));
    }
}
