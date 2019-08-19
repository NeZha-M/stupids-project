package nl.essent.automation.utils.data_automation;

public class DataFormatter {
    public static float convertToFloat(String floatString) {
        if (floatString.contains("-")) {
            floatString = floatString.trim();
            floatString = floatString.replace("-", "");
            floatString = "-" + floatString;
        }
        return Float.parseFloat(floatString);
    }
}
