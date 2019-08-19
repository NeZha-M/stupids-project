package nl.essent.automation.requests;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import nl.essent.automation.utils.EnvironmentVariables;

public class Documents {
    private static String documentsURL = "services/documents/document_id";
    @Steps
    EnvironmentVariables environmentVariables;

    private String getDocumentRequestURL(String account_id, String document_id, String version) {
        return documentsURL.replace("document_id", document_id) + "/v" + version + "?account_id=" + account_id;
    }

    public Response getDocument(String account_id, String document_id, String version) {
        return SerenityRest.given().get(getDocumentRequestURL(account_id, document_id, version));
    }
}
