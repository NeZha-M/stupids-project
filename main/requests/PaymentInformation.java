package nl.essent.automation.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import org.json.JSONObject;

public class PaymentInformation {

    private static final String UPDATE_PAYMENT_INFORMATION = "/contracting/paymentInformation/contract_account_id";

    private String getUpdatePaymentInformationURL(String contract_account_id, String version_number, String account_id) {
        return UPDATE_PAYMENT_INFORMATION.replaceAll("contract_account_id", contract_account_id) + "/v" + version_number + "?account_id=" + account_id;
    }

    public Response updatePaymentInformation(String incoming_payment_method, String iban, String contract_account_id, String version_number, String account_id) {
        return SerenityRest.given().contentType(ContentType.JSON).body(getUpdatePaymentDetailsPatchObject(incoming_payment_method, iban)).patch(getUpdatePaymentInformationURL(contract_account_id, version_number, account_id));
    }

    private String getUpdatePaymentDetailsPatchObject(String incoming_payment_method, String iban) {
        JSONObject json_object = new JSONObject();
        json_object.put("incoming_payment_method", incoming_payment_method);
        json_object.put("iban", iban);
        return json_object.toString();
    }

}
