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
    public static final String API_BLOCKCHAIN = "/api/blockchain";
    public static final String API_ACCOUNTS = "/api/accounts";
    public static final String API_TRANSACTIONS = "/api/transactions";
    public static final String API_BALANCE = "/api/balance";

    //Blockchain Constants
    public static final String HTTP_PROVIDER = "http://127.0.0.1:7545";
    public static final String GENERIC_EXCEPTION = "Exception encountered!";
    public static final String PLEASE_SUPPLY_REAL_DATA = "Please Supply Real Data!";

    public static final String PRIVATE_KEY = "246b859b2720c587ef03073e0c0966bec091fb430e707f77a36c1eb58e394e6d";

    public static final int GAS_PRICE = 200000000;
    public static final int GAS_LIMIT = 6721975;

    public static final String DEFAULT_ADDRESS = "0x281055afc982d96fab65b3a49cac8b878184cb16";
    public static final String DEFAULT_CONTRACT_ADDRESS = "00000000000000000000";
}
