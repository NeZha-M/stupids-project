package nl.essent.automation.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import nl.essent.automation.utils.JsonHandler;
import org.json.JSONObject;

public class UpdateBudgetBill {

    private static final String UPDATE_BUDGET_BILL = "/billing/budgetBillingPlans";

    private String getUpdateBudgetBillURL(String version_number, String account_id) {
        return UPDATE_BUDGET_BILL + "/v" + version_number + "?account_id=" + account_id;
    }

    public Response updateBudgetBill(int total_amount, String post_code, int house_number, String house_number_extension, String version_number, String account_id) {
        return SerenityRest.given().contentType(ContentType.JSON).body(updateBudgetBillPatchObject(total_amount, post_code, house_number, house_number_extension)).patch(getUpdateBudgetBillURL(version_number, account_id));
    }

    public Response updateBudgetBillWithWrongContentType(String version_number, String account_id, String invalid_content) {
        return SerenityRest.given().contentType(ContentType.JSON).body(invalid_content).patch(getUpdateBudgetBillURL(version_number, account_id));
    }

    private String updateBudgetBillPatchObject(int total_amount, String post_code, int house_number, String house_number_extension) {

        JSONObject address = new JSONObject();
        address.put("house_number", house_number);
        address.put("postcode", post_code);
        if (!(house_number_extension == null || house_number_extension == ""))
            address.put("house_number_extension", house_number_extension);
        JSONObject json_object = new JSONObject();
        json_object.put("total_amount", total_amount);
        json_object.put("address", address);
        return json_object.toString();
    }

}
