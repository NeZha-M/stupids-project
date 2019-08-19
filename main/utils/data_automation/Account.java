package nl.essent.automation.utils.data_automation;

import java.util.ArrayList;
import java.util.HashMap;

public class Account {
    public String getAccountBalance(String account_id) {
        ArrayList<String> openAmounts = getOpenItems(account_id).get("BETRW");
        float totalOpenAmount = 0;
        for (String openAmount : openAmounts) {
            totalOpenAmount = totalOpenAmount + DataFormatter.convertToFloat(openAmount);
        }
        return String.format("%.2f", totalOpenAmount);
    }

    private HashMap<String, ArrayList<String>> getOpenItems(String account_id) {
        TestDataAutomation testDataAutomation = new TestDataAutomation(false);
        ArrayList<String> outputFields = new ArrayList<String>() {{
            add("OPBEL");
            add("BETRW");
        }};
        ArrayList<String> queryStrings = new ArrayList<String>() {{
            add("GPART = '" + account_id + "'");
            add("AND AUGST = ' '");
        }};
        return testDataAutomation.ReadTable("DFKKOP", 1000, queryStrings, outputFields);
    }

    private ArrayList<String> getCRMDOrderHeader(String account_id) {
        TestDataAutomation testDataAutomation = new TestDataAutomation(true);
        ArrayList<String> outputFields = new ArrayList<String>() {{
            add("HEADER");
        }};
        ArrayList<String> queryStrings = new ArrayList<String>() {{
            add("PARTNER_NO = '" + account_id + "'");
            add("AND OBJECT_TYPE = 'BUS2000112'");
            add("AND PROCESS_TYPE_IX = 'ZISU'");
        }};
        return testDataAutomation.ReadTable("CRMD_ORDER_INDEX", 10, queryStrings, outputFields).get("HEADER");
    }

    public ArrayList<String> getCustomerContracts(String account_id) {
        ArrayList<String> crmd_order_header = getCRMDOrderHeader(account_id);
        System.out.println(crmd_order_header);
        TestDataAutomation testDataAutomation = new TestDataAutomation(true);
        ArrayList<String> outputFields = new ArrayList<String>() {{
            add("NUMBER_EXT");
        }};
        ArrayList<String> queryStrings = new ArrayList<String>() {{
            add("HEADER = '" + crmd_order_header + "'");
        }};
        return testDataAutomation.ReadTable("CRMD_ORDERADM_I", 20, queryStrings, outputFields).get("NUMBER_EXT");
    }

    public String getCustomerGUID(String account_id) throws Throwable {
        TestDataAutomation testDataAutomation = new TestDataAutomation(true);
        return testDataAutomation.getGUIDOfCustomer(account_id);
    }

    public HashMap<String, ArrayList<String>> getCustomerConsents(String account_id, String marketing_permission) throws Throwable {
        TestDataAutomation testDataAutomation = new TestDataAutomation(true);
        String partner_guid = getCustomerGUID(account_id);
        ArrayList<String> outputFields = new ArrayList<String>() {{
            add("CHANNEL");
            add("PERMISSION");
            add("VALUE");
        }};
        ArrayList<String> queryStrings = new ArrayList<String>() {{
            add("PARTNERGUID = '" + partner_guid + "'");
            add("AND CHANNEL = '" + marketing_permission + "'");
        }};
        return testDataAutomation.ReadTable("CRMM_BUT_MKTPERM", 15, queryStrings, outputFields);
    }
}
