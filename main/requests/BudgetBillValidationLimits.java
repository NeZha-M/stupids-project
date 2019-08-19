package nl.essent.automation.requests;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class BudgetBillValidationLimits {

    public static String budgetBillValidationLimitsURL = "/accounts/account_id/supplyAddresses/premise/budgetBillingPlanValidationLimits/v1";

    public String getBudgetBillValidationLimitsURL(String account_id,String premise) {
       String URL_with_account_id=budgetBillValidationLimitsURL.replace("account_id",account_id);
       return URL_with_account_id.replace("premise",premise);
    }

    public Response getBudgetBillValidationLimits(String account_id,String premise) {
        return SerenityRest.given().get(getBudgetBillValidationLimitsURL(account_id,premise));
    }
}
