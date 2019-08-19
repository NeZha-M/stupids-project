package nl.essent.automation.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Set;

public class JsonHandler {

    public static JSONObject convertToJsonObject(String jsonBodyString) {
        return new JSONObject(jsonBodyString);
    }

    public static JSONArray convertToJsonObjectArray(String jsonBodyString) {
        return new JSONArray(jsonBodyString);
    }

    public static JSONArray getJsonObjectArray(JSONObject jsonObject, String jsonArrayName, String... lookUp) {
        jsonObject = getJSONObject(jsonObject, lookUp);
        try {
            return jsonObject.getJSONArray(jsonArrayName);
        } catch (Throwable t) {
            return new JSONArray().put(jsonObject.getJSONObject(jsonArrayName));
        }
    }

    public static JSONObject readJSONFile(String fileLocation) {
        try {
            String jsonText = new String(Files.readAllBytes(Paths.get(fileLocation)), StandardCharsets.UTF_8);
            return convertToJsonObject(jsonText);
        } catch (Throwable t) {
            //File not found
        }
        return null;
    }

    public static void createJSONFile(String file_location, String json_string) throws Exception {
        File file = new File(file_location);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json_string);
        fileWriter.close();
    }

    public static LinkedHashMap<String, String> getKeyValuePairs(JSONObject jsonObject) {
        LinkedHashMap<String, String> keyValuePairs = new LinkedHashMap<>();
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet) {
            keyValuePairs.put(key, jsonObject.getString(key));
        }
        return keyValuePairs;
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String... lookUp) {
        for (String lookUpSearch : lookUp) {
            jsonObject = (JSONObject) jsonObject.get(lookUpSearch);
        }
        return jsonObject;
    }

    public static JSONObject getJSONObject(JSONArray jsonObjectArray, String searchKey, String searchValue, boolean isRepeatable) {
        JSONObject searchJSONObject = null;
        for (int jsonObject = 0; jsonObject < jsonObjectArray.length(); jsonObject++) {
            if (((JSONObject) jsonObjectArray.get(jsonObject)).get(searchKey).equals(searchValue)) {
                if (searchJSONObject == null) {
                    searchJSONObject = (JSONObject) jsonObjectArray.get(jsonObject);
                } else {
                    if (isRepeatable) {
                        searchJSONObject = (JSONObject) jsonObjectArray.get(jsonObject);
                    } else {
                        Assert.assertTrue(isRepeatable);
                    }
                }
            }
        }
        return searchJSONObject;
    }

    public static JSONObject getNullJSONObject(JSONArray jsonObjectArray, String searchKey) {
        JSONObject searchJSONObject = null;
        for (int jsonObject = 0; jsonObject < jsonObjectArray.length(); jsonObject++) {
            if (((JSONObject) jsonObjectArray.get(jsonObject)).get(searchKey).toString().equals("null")) {
                searchJSONObject = (JSONObject) jsonObjectArray.get(jsonObject);
                break;
            }
        }
        return searchJSONObject;
    }
}
