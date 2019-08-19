package nl.essent.automation.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import org.json.JSONObject;

import java.util.ArrayList;

public class VoiceLog {
    private static final String START_VOICE_LOG = "/communication/voicelogs/start";
    private static final String STOP_VOICE_LOG = "/communication/voicelogs/stop";

    private String getStartVoiceLogURL(String version_number, String account_id,String station_id) {
        return START_VOICE_LOG + "/v" + version_number + "?account_id=" + account_id+"&station_id="+ station_id;
    }

    public Response startVoiceLog(ArrayList<String> payload, String version_number, String account_id, String station_id) {
        return SerenityRest.given().contentType(ContentType.JSON).body(putVoiceLogAPIObject(payload)).put(getStartVoiceLogURL(version_number, account_id,station_id));
    }

    private String getStopVoiceLogURL(String version_number, String account_id,String station_id) {
        return STOP_VOICE_LOG + "/v" + version_number + "?account_id=" + account_id+"&station_id="+ station_id;
    }

    public Response stopVoiceLog(ArrayList<String> payload, String version_number, String account_id, String station_id) {
        return SerenityRest.given().contentType(ContentType.JSON).body(putVoiceLogAPIObject(payload)).put(getStopVoiceLogURL(version_number, account_id,station_id));
    }

    private String putVoiceLogAPIObject(ArrayList<String> payload) {
        JSONObject payload_voice_log = new JSONObject();
        payload_voice_log.put("house_number", payload.get(0));
        payload_voice_log.put("postcode", payload.get(1));
        if (!(payload.get(2) == null || payload.get(2) == ""))
            payload_voice_log.put("house_number_extension", payload.get(2));
        payload_voice_log.put("agent_id", payload.get(3));
        payload_voice_log.put("full_name", payload.get(4));
        payload_voice_log.put("remarks", payload.get(5));
        return payload_voice_log.toString();
    }
}
