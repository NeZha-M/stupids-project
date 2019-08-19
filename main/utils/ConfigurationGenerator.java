package nl.essent.automation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.File;

public class ConfigurationGenerator {

    public static void main(String[] args) throws Exception {
        generateConfiguration("entities", "es_get_grid_operator_details_v1", true);
    }

    private static void generateConfiguration(String node, String endpoint, boolean gi) throws Exception {
        String test_directory = System.getProperty("user.dir") + "\\src\\test";
        String data_directory = test_directory + "\\resources\\data" + "\\" + node + "\\" + endpoint;
        new File(data_directory).mkdir();
        String file_location_dev = data_directory + "\\" + endpoint + "_dev.json";
        String file_location_tst = data_directory + "\\" + endpoint + "_tst.json";
        String file_location_acc = data_directory + "\\" + endpoint + "_acc.json";
        JSONObject root_object = generateDefaultDataFile();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(root_object.toString());
        String prettyJsonString = gson.toJson(jsonElement);
        JsonHandler.createJSONFile(file_location_dev, prettyJsonString);
        JsonHandler.createJSONFile(file_location_tst, prettyJsonString);
        JsonHandler.createJSONFile(file_location_acc, prettyJsonString);
        if (gi) {
            String xml_request_directory = test_directory + "\\resources\\data\\request_xmls" + "\\" + node + "\\" + endpoint;
            new File(xml_request_directory).mkdir();
            String request_xml_directory = xml_request_directory + "\\" + endpoint + ".xml";
            Document xml_document = XmlHandler.createXMLDocument();
            XmlHandler.updateXmlDocument(xml_document, request_xml_directory);
        }
    }

    private static JSONObject generateDefaultDataFile() throws Exception {
        JSONObject root_object = new JSONObject();
        JSONObject essent_object = new JSONObject();
        JSONObject energiedirect_object = new JSONObject();
        JSONObject accounts_object = new JSONObject();
        JSONObject default_user_object_essent = new JSONObject();
        default_user_object_essent.put("account_id", "0171597320");
        essent_object.put("default-user", default_user_object_essent);
        JSONObject default_user_object_energiedirect = new JSONObject();
        default_user_object_energiedirect.put("account_id", "0132123098");
        energiedirect_object.put("default-user", default_user_object_energiedirect);
        accounts_object.put("essent", essent_object);
        accounts_object.put("energiedirect", energiedirect_object);
        root_object.put("accounts", accounts_object);
        return root_object;
    }
}
