package tss.orchestrator.utils.constants;

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

    public static final String TOKEN_ADDRESS = "0x93055039d1D0868D55911AD2752A655c3d719aF2";

    public static final int GAS_PRICE = 200000000;
    public static final int GAS_LIMIT = 6721975;

    public enum ContractState {
        NONE,
        INITIALIZED,
        FUNDED,
        ACTIVATED,
        DEACTIVATED
    }
}
