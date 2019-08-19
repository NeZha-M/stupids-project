package nl.essent.automation.utils.data_automation;

import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import nl.essent.automation.data_mappers.DataMapper;
import nl.essent.automation.utils.EnvironmentVariables;
import nl.essent.automation.utils.SessionVariableHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class TestDataAutomation {

    private final String DESTINATION_NAME = "build/ABAP_AS_WITH_POOL";

    public TestDataAutomation(boolean isItSapCRM) {
        Properties connectionProperties = getConnectionProperties(isItSapCRM);
        createDestinationDataFile(DESTINATION_NAME, connectionProperties);
    }

    private static void createDestinationDataFile(String destinationName, Properties connectProperties) {
        try {
            File destCfg = new File(destinationName + ".jcoDestination");
            FileOutputStream fileOutputStream = new FileOutputStream(destCfg, false);
            connectProperties.store(fileOutputStream, "Only for Tests");
            fileOutputStream.close();
        } catch (Throwable t) {
            throw new RuntimeException("Unable to create the destination file", t);
        }
    }

    public HashMap<String, ArrayList<String>> ReadTable(String tableName, int numberOfOutputRecords, ArrayList<String> queryStrings, ArrayList<String> outputFields) {
        JCoDestination jcoDestination = null;
        try {
            jcoDestination = JCoDestinationManager.getDestination(DESTINATION_NAME);
            JCoFunction jcoFunction = getJCoFunction(jcoDestination, tableName, numberOfOutputRecords);
            JCoTable filterOptions = jcoFunction.getTableParameterList().getTable("OPTIONS");
            for (String queryString : queryStrings) {
                filterOptions.appendRow();
                filterOptions.setValue("TEXT", queryString);
            }
            JCoTable returnFields = jcoFunction.getTableParameterList().getTable("FIELDS");
            for (String outputField : outputFields) {
                returnFields.appendRow();
                returnFields.setValue("FIELDNAME", outputField);
            }
            jcoFunction.execute(jcoDestination);
            JCoTable jcoTable = jcoFunction.getTableParameterList().getTable("DATA");
            return processOutputRecords(jcoTable, outputFields);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (jcoDestination != null) {
                try {
                    JCoContext.end(jcoDestination);
                } catch (JCoException e) {
                    System.out.println("JCO Destination doesn't exist");
                }
            }
        }
    }

    private HashMap<String, ArrayList<String>> processOutputRecords(JCoTable jCoTable, ArrayList<String> outputFields) {
        int noOfOutputRows = jCoTable.getNumRows();
        if (noOfOutputRows == 0) {
            return null;
        } else {
            HashMap<String, ArrayList<String>> searchOutput = new HashMap<>();
            for (String outputField : outputFields) {
                searchOutput.put(outputField, new ArrayList<>());
            }
            for (int rowNumber = 0; rowNumber < noOfOutputRows; rowNumber++) {
                jCoTable.setRow(rowNumber);
                JCoFieldIterator fieldIterator = jCoTable.getFieldIterator();
                while (fieldIterator.hasNextField()) {
                    JCoField jCoField = fieldIterator.nextField();
                    String[] outputFieldValues = jCoField.getString().split(";");
                    for (int outputFieldValue = 0; outputFieldValue < outputFieldValues.length; outputFieldValue++) {
                        searchOutput.get(outputFields.get(outputFieldValue)).add(outputFieldValues[outputFieldValue]);
                    }
                }
            }
            return searchOutput;
        }

    }

    private JCoFunction getJCoFunction(JCoDestination jCoDestination, String tableName, int numberOfOutputRecords) throws Exception {
        JCoContext.begin(jCoDestination);
        JCoRepository sapRepository = jCoDestination.getRepository();
        JCoFunctionTemplate template = sapRepository.getFunctionTemplate("RFC_READ_TABLE");
        JCoFunction jcoFunction = template.getFunction();
        jcoFunction.getImportParameterList().setValue("QUERY_TABLE", tableName);
        jcoFunction.getImportParameterList().setValue("DELIMITER", ";");
        jcoFunction.getImportParameterList().setValue("ROWSKIPS", Integer.valueOf(0));
        jcoFunction.getImportParameterList().setValue("ROWCOUNT", Integer.valueOf(numberOfOutputRecords));
        return jcoFunction;
    }

    public String getGUIDOfCustomer(String customer_number) throws Throwable {
        JCoDestination jcoDestination;
        JCoRepository sapRepository;
        jcoDestination = JCoDestinationManager.getDestination(this.DESTINATION_NAME);
        JCoDestinationManager.getDestination(this.DESTINATION_NAME);
        try {
            sapRepository = jcoDestination.getRepository();
            JCoFunctionTemplate template = sapRepository.getFunctionTemplate("BUPA_NUMBERS_GET");
            JCoFunction function = template.getFunction();
            function.getImportParameterList().setValue("IV_PARTNER", customer_number);
            function.execute(jcoDestination);
            return function.getExportParameterList().getString("EV_PARTNER_GUID");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                JCoContext.end(jcoDestination);
            } catch (JCoException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Properties getConnectionProperties(boolean isItSAPCRM) {
        Properties connectionProperties = new Properties();
        String environment = "sap_";
        if (isItSAPCRM) {
            environment = environment + "crm_";
        } else {
            environment = environment + "isu_";
        }
        environment = environment + SessionVariableHolder.environment_under_test;
        DataMapper dataMapper = new DataMapper();
        connectionProperties.setProperty(DestinationDataProvider.JCO_ASHOST, dataMapper.getCredentials(environment, "jco_host"));
        connectionProperties.setProperty(DestinationDataProvider.JCO_SYSNR, dataMapper.getCredentials(environment, "jco_system_number"));
        connectionProperties.setProperty(DestinationDataProvider.JCO_USER, dataMapper.getCredentials(environment, "username"));
        connectionProperties.setProperty(DestinationDataProvider.JCO_PASSWD, dataMapper.getCredentials(environment, "password"));
        connectionProperties.setProperty(DestinationDataProvider.JCO_LANG, dataMapper.getCredentials(environment, "language"));
        connectionProperties.setProperty(DestinationDataProvider.JCO_CLIENT, getSAPBrandValue());
        return connectionProperties;
    }

    private String getSAPBrandValue() {
        if (SessionVariableHolder.brand_under_test.equals(EnvironmentVariables.brand_energie_direct)) {
            return EnvironmentVariables.energiedirect_sap_brand_value;
        } else if (SessionVariableHolder.brand_under_test.equals(EnvironmentVariables.brand_essent)) {
            return EnvironmentVariables.essent_sap_brand_value;
        } else {
            return null;
        }
    }
}
