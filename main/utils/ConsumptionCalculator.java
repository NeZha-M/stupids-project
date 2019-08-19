package nl.essent.automation.utils;

import java.util.LinkedHashMap;

public class ConsumptionCalculator {
    public static final String ELECTRICITY_CONSUMPTION_VALUE = "electricity";
    public static final String GAS_CONSUMPTION_VALUE = "gas";
    private static final String CONSUMPTION_CATEGORY_HIGH = "total_usage_high";
    private static final String CONSUMPTION_CATEGORY_LOW = "total_usage_low";
    private static final double VACANCY_YES_PERCENTAGE = 0.1;
    private static final int DEFAULT_CONSUMPTION_ELECTRICITY = 3500;
    private static final int DEFAULT_CONSUMPTION_GAS = 1800;
    private static final int EXTRA_USAGE_ELECTRICITY = 500;
    private static final int EXTRA_USAGE_GAS = 60;
    private static long ELECTRICITY_CONSUMPTION = 0;
    private static long GAS_CONSUMPTION = 0;

    public static LinkedHashMap<String, Long> getEstimatedConsumption(boolean vacancy, boolean new_contract, int electricity_consumption, int gas_consumption, int number_of_people, boolean day_time_usage, String consumption_category, String premise_category, String construction_year) {
        LinkedHashMap<String, Long> estimatedConsumption = new LinkedHashMap<>();
        if (vacancy) {
            if (new_contract) {
                estimatedConsumption.put(ELECTRICITY_CONSUMPTION_VALUE, Math.round(DEFAULT_CONSUMPTION_ELECTRICITY * VACANCY_YES_PERCENTAGE));
                estimatedConsumption.put(GAS_CONSUMPTION_VALUE, Math.round(DEFAULT_CONSUMPTION_GAS * VACANCY_YES_PERCENTAGE));
            } else {
                estimatedConsumption.put(ELECTRICITY_CONSUMPTION_VALUE, Math.round(electricity_consumption * VACANCY_YES_PERCENTAGE));
                estimatedConsumption.put(GAS_CONSUMPTION_VALUE, Math.round(gas_consumption * VACANCY_YES_PERCENTAGE));
            }
            return estimatedConsumption;
        } else {
            setConsumption(number_of_people);
            double day_time_usage_percentage_electricity;
            double day_time_usage_percentage_gas;
            double consumption_category_percentage_electricity;
            double consumption_category_percentage_gas;
            if (!day_time_usage) {
                day_time_usage_percentage_electricity = 0.0;
                day_time_usage_percentage_gas = 0.0;
            } else {
                day_time_usage_percentage_electricity = 0.05;
                day_time_usage_percentage_gas = 0.1;
            }
            if (consumption_category.equals(CONSUMPTION_CATEGORY_HIGH)) {
                consumption_category_percentage_electricity = 0.2;
                consumption_category_percentage_gas = 0.05;
            } else if (consumption_category.equals(CONSUMPTION_CATEGORY_LOW)) {
                consumption_category_percentage_electricity = -0.2;
                consumption_category_percentage_gas = -0.2;
            } else {
                consumption_category_percentage_electricity = 0;
                consumption_category_percentage_gas = 0;
            }
            estimatedConsumption.put(ELECTRICITY_CONSUMPTION_VALUE, Math.round(ELECTRICITY_CONSUMPTION * (1 + day_time_usage_percentage_electricity + consumption_category_percentage_electricity)));
            double expected_gas = GAS_CONSUMPTION + getAdditionalGasConsumption(premise_category, construction_year);
            expected_gas = expected_gas * (1 + day_time_usage_percentage_gas + consumption_category_percentage_gas);
            estimatedConsumption.put(GAS_CONSUMPTION_VALUE, Math.round(expected_gas));
            return estimatedConsumption;
        }
    }

    private static void setConsumption(int number_of_people) {
        switch (number_of_people) {
            case 1:
                ELECTRICITY_CONSUMPTION = 2220;
                GAS_CONSUMPTION = 315;
                break;
            case 2:
                ELECTRICITY_CONSUMPTION = 3095;
                GAS_CONSUMPTION = 375;
                break;
            case 3:
                ELECTRICITY_CONSUMPTION = 3875;
                GAS_CONSUMPTION = 435;
                break;
            case 4:
                ELECTRICITY_CONSUMPTION = 4345;
                GAS_CONSUMPTION = 495;
                break;
            case 5:
                ELECTRICITY_CONSUMPTION = 4910;
                GAS_CONSUMPTION = 555;
                break;
            case 6:
                ELECTRICITY_CONSUMPTION = 5295;
                GAS_CONSUMPTION = 615;
                break;
            default:
                ELECTRICITY_CONSUMPTION = 5295 + (number_of_people - 6) * EXTRA_USAGE_ELECTRICITY;
                GAS_CONSUMPTION = 615 + (number_of_people - 6) * EXTRA_USAGE_GAS;
                break;
        }
    }

    private static long getAdditionalGasConsumption(String premise_category, String construction_year) {
        LinkedHashMap<String, Long> gas_additional_consumption = new LinkedHashMap<>();
        gas_additional_consumption.put("detached;before_1945", 2405L);
        gas_additional_consumption.put("detached;1945_1980", 2725L);
        gas_additional_consumption.put("detached;after_1980", 1835L);
        gas_additional_consumption.put("detached;unknown", 2725L);
        gas_additional_consumption.put("semi_detached;before_1945", 2205L);
        gas_additional_consumption.put("semi_detached;1945_1980", 1890L);
        gas_additional_consumption.put("semi_detached;after_1980", 1390L);
        gas_additional_consumption.put("semi_detached;unknown", 1890L);
        gas_additional_consumption.put("corner_house;before_1945", 1950L);
        gas_additional_consumption.put("corner_house;1945_1980", 1835L);
        gas_additional_consumption.put("corner_house;after_1980", 1245L);
        gas_additional_consumption.put("corner_house;unknown", 1835L);
        gas_additional_consumption.put("row_house;before_1945", 1595L);
        gas_additional_consumption.put("row_house;1945_1980", 1485L);
        gas_additional_consumption.put("row_house;after_1980", 1055L);
        gas_additional_consumption.put("row_house;unknown", 1485L);
        gas_additional_consumption.put("apartment;before_1945", 1780L);
        gas_additional_consumption.put("apartment;1945_1980", 1005L);
        gas_additional_consumption.put("apartment;after_1980", 780L);
        gas_additional_consumption.put("apartment;unknown", 1005L);
        gas_additional_consumption.put("garage;before_1945", 500L);
        gas_additional_consumption.put("garage;1945_1980", 500L);
        gas_additional_consumption.put("garage;after_1980", 500L);
        gas_additional_consumption.put("garage;unknown", 500L);
        gas_additional_consumption.put("unknown;before_1945", 1950L);
        gas_additional_consumption.put("unknown;1945_1980", 1835L);
        gas_additional_consumption.put("unknown;after_1980", 1245L);
        gas_additional_consumption.put("unknown;unknown", 1835L);
        return gas_additional_consumption.get(premise_category + ";" + construction_year);
    }
}
