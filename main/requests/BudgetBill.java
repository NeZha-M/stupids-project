package nl.essent.automation.requests;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class BudgetBill {

    private final String BUDGET_BILL_PLANS_URL = "/billing/budgetBillingPlans";

    private String getBudgetBillPlansURL(String account_id, String post_code, String house_number, String house_number_extension, String version) {
        if (house_number_extension == null || house_number_extension.equals(""))
            return BUDGET_BILL_PLANS_URL + "/v" + version + "?account_id=" + account_id + "&postcode=" + post_code + "&house_number=" + house_number;
        else
            return BUDGET_BILL_PLANS_URL + "/v" + version + "?account_id=" + account_id + "&postcode=" + post_code + "&house_number=" + house_number + "&house_number_extension=" + house_number_extension;
    }

    public Response getBudgetBillPlans(String account_id, String post_code, String house_number, String house_number_extension, String version) {
        return SerenityRest.given().get(getBudgetBillPlansURL(account_id, post_code, house_number, house_number_extension, version));
    }
}
