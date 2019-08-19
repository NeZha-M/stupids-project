package nl.essent.automation.requests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import nl.essent.automation.utils.EnvironmentVariables;
import nl.essent.automation.utils.SessionVariableHolder;
import nl.essent.automation.utils.StatusCodes;
import org.junit.Assert;

public class Authentication {
    private final String ssoAuthenticationClientIDTst = "aedc8b91-3d97-4f28-90e6-8b93a4bf1653";
    private final String ssoAuthenticationClientIDAcc = "96dae1ed-d8b8-498c-b452-0225e89c708e";

    private final String oauthGrantType = "password";
    private final String oauthTokenRequestContentType = "application/x-www-form-urlencoded";
    private final String iWelcomeBasicAuthentication = "Basic ZWRfc2VsZnNlcnZpY2VfbW9iaWxlX2FwcDo=";
    private final String iWelcomeAuthScope = "openid customer_id";
    private final String iWelcomeBaseURLAcc = "https://login-acc.energiedirect.nl";
    private final String iWelcomeBaseURLTst = "https://energiedirect.iwelcome.com";
    private final String ssoAuthenticationBaseURL = "https://cfs-test.rwe.com";
    private final String ssoTokenScope = "openid openid, profile";
    private final String accessTokenGrantType = "urn:ietf:params:oauth:grant-type:jwt-bearer";
    private final String iWelcomeTargetAPI = "api_gateway";
    private final String iWelcomeAPIType = "app";

    private final String axWayAuthenticationURL = "/api/oauth/token";
    private final String iWelcomeAuthenticationURL = "/am/oauth2/token";
    private final String ssoAuthenticationURL = "/adfs/oauth2/token";
    private final String iWelcomeDelegationTokenURL = "/am/oauth2/delegate";
    private final String ssoResourceRequestTst = "https://cockpit-claims-tst";
    private final String ssoResourceRequestAcc = "https://cockpit-claims-acc";

    public Response getSSOAccessToken(String username, String password) {
        String ssoAuthenticationClientID;
        String ssoResourceRequest;
        if (SessionVariableHolder.environment_under_test.equals(EnvironmentVariables.acc_environment)) {
            ssoAuthenticationClientID = ssoAuthenticationClientIDAcc;
            ssoResourceRequest = ssoResourceRequestAcc;
        } else {
            ssoAuthenticationClientID = ssoAuthenticationClientIDTst;
            ssoResourceRequest = ssoResourceRequestTst;
        }
        String storeBaseURI = RestAssured.baseURI;
        RestAssured.baseURI = ssoAuthenticationBaseURL;
        Response authenticationResponse = SerenityRest.given().contentType(oauthTokenRequestContentType).
                formParam("grant_type", oauthGrantType).
                formParam("client_id", ssoAuthenticationClientID).
                formParam("username", "GROUP\\" + username).
                formParam("password", password).
                formParam("scope", ssoTokenScope).
                formParam("resource", ssoResourceRequest).
                post(ssoAuthenticationURL);
        RestAssured.baseURI = storeBaseURI;
        return authenticationResponse;
    }

    public Response getAxWayAccessToken(String username, String password, String client_id, String client_secret) {
        return SerenityRest.given().contentType(oauthTokenRequestContentType + "; charset=UTF-8").
                formParam("grant_type", oauthGrantType).
                formParam("client_id", client_id).
                formParam("client_secret", client_secret).
                formParam("username", username).
                formParam("password", password).
                post(axWayAuthenticationURL);
    }

    public Response getiWelcomeAccessToken(String username, String password) {
        String storeBaseURI = RestAssured.baseURI;
        if (SessionVariableHolder.brand_under_test.equals(EnvironmentVariables.acc_environment)) {
            RestAssured.baseURI = iWelcomeBaseURLAcc;
        } else {
            RestAssured.baseURI = iWelcomeBaseURLTst;
        }
        Response authenticationResponse = SerenityRest.given().contentType(oauthTokenRequestContentType + "; charset=UTF-8").
                header("Authorization", iWelcomeBasicAuthentication).
                formParam("grant_type", oauthGrantType).
                formParam("scope", iWelcomeAuthScope).
                formParam("username", username).
                formParam("password", password).
                post(iWelcomeAuthenticationURL);
        Assert.assertEquals(StatusCodes.REQUEST_OK_SUCCESS_CODE, authenticationResponse.getStatusCode());
        authenticationResponse = getAccessTokenFromRefreshToken(authenticationResponse.then().extract().path("refresh_token"));
        RestAssured.baseURI = storeBaseURI;
        return authenticationResponse;
    }

    private Response getAccessTokenFromRefreshToken(String refreshToken) {
        return SerenityRest.given().contentType(oauthTokenRequestContentType + "; charset=UTF-8").
                header("Authorization", iWelcomeBasicAuthentication).
                formParam("refresh_token", refreshToken).
                formParam("grant_type", accessTokenGrantType).
                formParam("target", iWelcomeTargetAPI).
                formParam("api_type", iWelcomeAPIType).
                formParam("scope", ssoTokenScope).
                post(iWelcomeDelegationTokenURL);
    }
}
