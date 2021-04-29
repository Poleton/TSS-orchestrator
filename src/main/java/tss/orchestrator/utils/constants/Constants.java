package tss.orchestrator.utils.constants;

public class Constants {

    //Login Paths
    public static final String API_LOGIN = "/user";
    public static final String API_AUTH = "/auth";

    //UI Paths
    public static final String API_USERS = "/users";
    public static final String API_POLICIES = "/{userId}/api/policies";
    public static final String API_SMART_POLICIES = "/{userId}/api/smart-policies";
    public static final String API_SMART_POLICY = "/{userId}/api/smart-policies/{smartId}";

    //BlockChain Paths
    public static final String API_BLOCKCHAIN = "/blockchain";
    public static final String API_SENSORS = "/api/sensors";

    //Blockchain Constants
    public static final String HTTP_PROVIDER = "http://127.0.0.1:7545";
    public static final String GENERIC_EXCEPTION = "Exception encountered!";
    public static final String PLEASE_SUPPLY_REAL_DATA = "Please Supply Real Data!";

    public static final String PRIVATE_KEY = "7396c32490d627a95d33c17db86b4897fe6770026bbe2f10f3403a236302af22";

    public static final int GAS_PRICE = 200000000;
    public static final int GAS_LIMIT = 6721975;

    public static final String DEFAULT_ADDRESS = "0x281055afc982d96fab65b3a49cac8b878184cb16";
    public static final String DEFAULT_CONTRACT_ADDRESS = "00000000000000000000";
}
