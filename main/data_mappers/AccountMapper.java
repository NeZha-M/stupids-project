package nl.essent.automation.data_mappers;

import net.thucydides.core.annotations.Steps;
import nl.essent.automation.utils.DataFactory;
import nl.essent.automation.utils.SessionVariableHolder;
import nl.essent.automation.utils.data_automation.Account;
import org.json.JSONArray;

import java.util.ArrayList;

public class AccountMapper {

    @Steps
    Account account;

    public String getAccountData(String dataKey) {
        if (SessionVariableHolder.account_under_test.equals("")) {
            SessionVariableHolder.account_under_test = "default-user";
        }
        return (String) DataFactory.get(true, dataKey, "accounts", SessionVariableHolder.brand_under_test, SessionVariableHolder.account_under_test, dataKey);
    }

    public JSONArray getJSONArray(String jsonArrayName) {
        return (JSONArray) DataFactory.get(true, jsonArrayName, "accounts", SessionVariableHolder.brand_under_test, SessionVariableHolder.account_under_test, jsonArrayName);
    }

    public String getAccountBalance() {
        if (SessionVariableHolder.automatic_data_verification) {
            return account.getAccountBalance(getAccountData("account_id"));
        } else {
            return getAccountData("account_balance");
        }
    }

    public ArrayList<String> getMultipleFields(int numberOfFields, String jsonObjectName, String fieldStartName) {
        ArrayList<String> fieldValues = new ArrayList<>();
        for (int fieldCount = 1; fieldCount <= numberOfFields; fieldCount++) {
            fieldValues.add((String) DataFactory.get(true, "No Field", "accounts", SessionVariableHolder.brand_under_test, SessionVariableHolder.account_under_test, jsonObjectName, fieldStartName + fieldCount));
        }
        return fieldValues;
    }

    public ArrayList<String> getMultipleFields(String jsonObjectName, String fieldStartName) {
        ArrayList<String> fieldValues = new ArrayList<>();
        for (int fieldCount = 1; ; fieldCount++) {
            String fieldValue = (String) DataFactory.get(true, "No Field", "accounts", SessionVariableHolder.brand_under_test, SessionVariableHolder.account_under_test, jsonObjectName, fieldStartName + fieldCount);
            if (fieldValue == null || fieldValue.equals("")) {
                break;
            }
            fieldValues.add(fieldValue);
        }
        return fieldValues;
    }
}
