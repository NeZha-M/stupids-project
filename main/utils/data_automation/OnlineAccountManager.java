package nl.essent.automation.utils.data_automation;

import net.thucydides.core.annotations.Steps;
import nl.essent.automation.data_mappers.DataMapper;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;

public class OnlineAccountManager {

    @Steps
    DataMapper dataMapper;

    private static Statement GetDatabaseConnection(String dataBaseAddress, String userName, String password) throws Throwable {
        Connection dataBaseConnection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dataBaseConnection = DriverManager.getConnection(dataBaseAddress, userName, password);
            dataBaseConnection.setAutoCommit(true);
            return dataBaseConnection.createStatement();
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    private static LinkedHashMap<String, String> ExecuteDatabaseQuery(String[] outputFields, Statement dataBaseStatement, String dataBaseQuery) {
        LinkedHashMap<String, String> queryResults = new LinkedHashMap<String, String>();
        for (String outputField : outputFields) {
            queryResults.put(outputField, "");
        }
        try {
            boolean firstRow = true;
            ResultSet resultSet = dataBaseStatement.executeQuery(dataBaseQuery);
            while (resultSet.next() == true) {
                for (String outputField : outputFields) {
                    if (firstRow) {
                        queryResults.put(outputField, resultSet.getString(outputField));
                        firstRow = false;
                    } else {
                        queryResults.put(outputField, queryResults.get(outputField) + ";" + resultSet.getString(outputField));
                    }
                }
            }
            return queryResults;
        } catch (Throwable t) {
            return null;
        }
    }

    private static boolean ExecuteDatabaseUpdate(Statement dataBaseStatement, String dataBaseQuery) {
        try {
            dataBaseStatement.executeUpdate(dataBaseQuery);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean ResetPassword(String username, String password) throws Throwable {
        String database_url = dataMapper.getCredentials("wus-db", "server_url");
        String database_username = dataMapper.getCredentials("wus-db", "username");
        String database_password = dataMapper.getCredentials("wus-db", "password");
        Statement statement = GetDatabaseConnection(database_url, database_username, database_password);
        String query = "SELECT Salt FROM WebUserStore WHERE UserID='" + username + "'";
        LinkedHashMap<String, String> results = ExecuteDatabaseQuery(new String[]{"Salt"}, statement, query);
        statement.close();
        String salted_password = results.get("Salt") + password;
        byte[] bytesOfMessage = salted_password.getBytes("UTF-8");
        MessageDigest message_digest = MessageDigest.getInstance("MD5");
        byte[] salt_password = message_digest.digest(bytesOfMessage);
        StringBuilder salted_string = new StringBuilder();
        for (byte byte_part : salt_password) {
            salted_string.append(String.format("%02x", byte_part));
        }
        String update_query = "UPDATE WebUserStore SET Password = '" + salted_string.toString() + "' WHERE UserID = '" + username + "'";
        statement = GetDatabaseConnection(database_url, database_username, database_password);
        boolean database_updated = ExecuteDatabaseUpdate(statement, update_query);
        statement.close();
        return database_updated;
    }
}
