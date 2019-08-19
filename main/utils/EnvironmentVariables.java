package nl.essent.automation.utils;

import io.restassured.RestAssured;

public class EnvironmentVariables {

    public static final String brand_energie_direct = "energiedirect";
    public static final String brand_essent = "essent";
    public static final String energiedirect_sap_brand_value = "050";
    public static final String essent_sap_brand_value = "040";
    public static final String tst_environment = "tst";
    public static final String acc_environment = "acc";
    public static final String dev_environment = "dev";

    public static void setAPIEnvironmentBaseURL() {
        String baseURI = "https://api-" + SessionVariableHolder.environment_under_test + "." + SessionVariableHolder.brand_under_test + ".nl";
        SessionVariableHolder.api_environment_base_url = baseURI;
        RestAssured.baseURI = baseURI;
    }
}
