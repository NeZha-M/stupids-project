package nl.essent.automation.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.ProxySpecification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JiraIntegration {
    public static void main(String[] args) throws Exception {
        RestAssured.proxy(ProxySpecification.host("proxy.rwe.com").withPort(8080));
        String jira_project = System.getProperty("jira_project");
        String jira_basic_auth = System.getProperty("jira_basic_auth");
        String destination_directory = System.getProperty("destination_directory");
        Response jira_features = RestAssured.given().header("Authorization", jira_basic_auth).get("https://jira.essent.nl/jira/rest/cucumber/1.0/project/" + jira_project + "/features?manual=false");
        InputStream jira_features_response = jira_features.getBody().asInputStream();
        ZipInputStream zip_input_stream = new ZipInputStream(jira_features_response);
        ZipEntry feature_file = zip_input_stream.getNextEntry();
        byte[] buffer = new byte[1024];
        while (feature_file != null) {
            String feature_name = feature_file.getName();
            File feature_file_entry = new File(destination_directory + File.separator + feature_name);
            FileOutputStream file_output_stream = new FileOutputStream(feature_file_entry);
            int buffer_length;
            while ((buffer_length = zip_input_stream.read(buffer)) > 0) {
                file_output_stream.write(buffer, 0, buffer_length);
            }
            file_output_stream.close();
            zip_input_stream.closeEntry();
            feature_file = zip_input_stream.getNextEntry();
        }
    }
}
