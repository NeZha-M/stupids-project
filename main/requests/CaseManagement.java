package nl.essent.automation.requests;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class CaseManagement {

    private final String CASE_MANAGEMENT_CONTRACTS_URL = "/caseManagement/contracts";

    private String getContractCaseManagementURL(String account_id, String version) {
        return CASE_MANAGEMENT_CONTRACTS_URL + "/v" + version + "?account_id=" + account_id;
    }

    public Response retrieveRunningContractProcesses(String account_id, String version) {
        return SerenityRest.given().get(getContractCaseManagementURL(account_id, version));
    }
}
