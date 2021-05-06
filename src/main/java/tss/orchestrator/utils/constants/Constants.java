package tss.orchestrator.utils.constants;

import java.math.BigInteger;

public class  Constants {

    //Login Paths
    public static final String API_LOGIN = "/user";
    public static final String API_AUTH = "/auth";


    //UI Paths
    public static final String API_USERS = "/users";
    public static final String API_POLICIES = "/{userId}/api/policies";
    public static final String API_SMART_POLICIES = "/{userId}/api/smart-policies";
    public static final String API_SMART_POLICY_ALERT = "/{userId}/api/smart-policies/{smartId}/alerts";
    public static final String API_SMART_POLICY_DEACTIVATION = "/{userId}/api/smart-policies/{smartId}/deactivation";


    //BlockChain Paths
    public static final String API_BLOCKCHAIN = "/blockchain";
    public static final String API_SENSORS = "/api/sensors";

    //Blockchain Constants
    public static final String HTTP_PROVIDER = "http://127.0.0.1:7545";

    public static final String TOKEN_ADDRESS = "0x578bB0E7F3FF8ca80352dE634C3bf4221e9B24Ba";

    public static final int GAS_PRICE = 200000000;
    public static final int GAS_LIMIT = 6721975;

    public static final BigInteger zeros = new BigInteger("1000000000000000000");

    public enum ContractState {
        NONE,
        INITIALIZED,
        FUNDED,
        ACTIVATED,
        DEACTIVATED
    }

    public enum SensorType {
        TEMPERATURE,
        PRESSURE,
        ACCELERATION,
        HUMIDITY
    }
}
