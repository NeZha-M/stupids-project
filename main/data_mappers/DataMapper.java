package nl.essent.automation.data_mappers;

import nl.essent.automation.utils.DataFactory;
import nl.essent.automation.utils.JsonHandler;
import nl.essent.automation.utils.SessionVariableHolder;
import org.json.JSONObject;

public class DataMapper {

    public String getCredentials(String applicationName, String fieldName) {
        return (String) DataFactory.get(false, fieldName, "accounts", "credentials", applicationName, fieldName);
    }

    public String getContent(String featureName, String contentField) {
        return (String) DataFactory.get(false, contentField, "accounts", "content", featureName, contentField);
    }

    public JSONObject getContentObject(String featureName, String contentField) throws Exception {
        org.json.simple.JSONObject simple_json_object = (org.json.simple.JSONObject) DataFactory.get(false, contentField, "accounts", "content", featureName, contentField);
        return JsonHandler.convertToJsonObject(simple_json_object.toString());
    }

    public int getThrottlingLimit() {
        return Integer.parseInt((String) DataFactory.get(false, "0", "accounts", "content", "throttling_limits", SessionVariableHolder.end_point_under_test));
    }
}
