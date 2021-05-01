package tss.orchestrator.models.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class SmartInsurancePolicy extends Contract {
    public static final String BINARY = "0x608060405260006006556000600c556000600e5560006010553480156200002557600080fd5b50604051620014033803806200140383398181016040526101008110156200004c57600080fd5b810190808051906020019092919080519060200190929190805190602001909291908051906020019092919080519060200190929190805190602001909291908051906020019092919080516040519392919084640100000000821115620000b357600080fd5b83820191506020820185811115620000ca57600080fd5b8251866001820283011164010000000082111715620000e857600080fd5b8083526020830192505050908051906020019080838360005b838110156200011e57808201518184015260208101905062000101565b50505050905090810190601f1680156200014c5780820380516001836020036101000a031916815260200191505b5060405250505033600060016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555087600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555086600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555085600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600560ff16600a0a8502600481905550600560ff16600a0a84026005819055506040518060a001604052808481526020016000815260200183815260200160008152602001828152506007600082015181600001556020820151816001015560408201518160020155606082015181600301556080820151816004019080519060200190620002e89291906200031e565b5090505060008060006101000a81548160ff021916908360038111156200030b57fe5b02179055505050505050505050620003c4565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200036157805160ff191683800117855562000392565b8280016001018555821562000392579182015b828111156200039157825182559160200191906001019062000374565b5b509050620003a19190620003a5565b5090565b5b80821115620003c0576000816000905550600101620003a6565b5090565b61102f80620003d46000396000f3fe608060405234801561001057600080fd5b506004361061014d5760003560e01c80637decc55b116100c3578063b4a2d3861161007c578063b4a2d38614610576578063bd097e2114610594578063df2bbd901461059e578063e20ac1cc146105ea578063fd4263021461060b578063ff458553146106295761014d565b80637decc55b146103db578063890357301461041d57806389cf3204146104bc5780638da5cb5b146104f0578063aace20b714610524578063abff0110146105425761014d565b806335c2bd631161011557806335c2bd63146102b55780633eec0758146102f9578063412f2e82146103315780635627c2f814610381578063642cd7871461039f5780637742faa0146103bd5761014d565b806309dfcd0014610152578063109e94cf146101b05780631be141e0146101e457806322a45cd01461021c5780632ac08a931461025e575b600080fd5b61017e6004803603602081101561016857600080fd5b810190808035906020019092919050505061067f565b604051808681526020018581526020018481526020018381526020018281526020019550505050505060405180910390f35b6101b86106b5565b604051808273ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61021a600480360360408110156101fa57600080fd5b8101908080359060200190929190803590602001909291905050506106db565b005b6102486004803603602081101561023257600080fd5b81019080803590602001909291905050506107b1565b6040518082815260200191505060405180910390f35b61028a6004803603602081101561027457600080fd5b8101908080359060200190929190505050610866565b6040518085815260200184815260200183815260200182815260200194505050505060405180910390f35b6102e1600480360360208110156102cb57600080fd5b8101908080359060200190929190505050610896565b60405180821515815260200191505060405180910390f35b61032f6004803603604081101561030f57600080fd5b810190808035906020019092919080359060200190929190505050610993565b005b61035d6004803603602081101561034757600080fd5b8101908080359060200190929190505050610a14565b60405180848152602001838152602001828152602001935050505060405180910390f35b610389610a3e565b6040518082815260200191505060405180910390f35b6103a7610a44565b6040518082815260200191505060405180910390f35b6103c5610a4a565b6040518082815260200191505060405180910390f35b61041b600480360360608110156103f157600080fd5b81019080803590602001909291908035906020019092919080359060200190929190505050610a50565b005b610425610bec565b6040518086815260200185815260200184815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561047d578082015181840152602081019050610462565b50505050905090810190601f1680156104aa5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b6104c4610ca8565b604051808273ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6104f8610cce565b604051808273ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61052c610cf4565b6040518082815260200191505060405180910390f35b61054a610cfa565b604051808273ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61057e610d20565b6040518082815260200191505060405180910390f35b61059c610d26565b005b6105d4600480360360408110156105b457600080fd5b810190808035906020019092919080359060200190929190505050610d28565b6040518082815260200191505060405180910390f35b6105f2610d69565b604051808260ff16815260200191505060405180910390f35b610613610d6e565b6040518082815260200191505060405180910390f35b61067d600480360360a081101561063f57600080fd5b810190808035906020019092919080359060200190929190803590602001909291908035906020019092919080359060200190929190505050610d74565b005b60116020528060005260406000206000915090508060000154908060010154908060020154908060030154908060040154905085565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60006106e8836001610d28565b905060005b600c548110156107ab5781600d600083815260200190815260200160002060000154141561079e5760405180606001604052808581526020018481526020016000815250600f6000600e5481526020019081526020016000206000820151816000015560208201518160010155604082015181600201559050506001600e600082825401925050819055506001600d6000838152602001908152602001600020600301600082825401925050819055505b80806001019150506106ed565b50505050565b6000600260038111156107c057fe5b60008054906101000a900460ff1660038111156107d957fe5b1461082f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526027815260200180610fd36027913960400191505060405180910390fd5b8160076003018190555060036000806101000a81548160ff0219169083600381111561085757fe5b02179055506006549050919050565b600d6020528060005260406000206000915090508060000154908060010154908060020154908060030154905084565b6000806000905060005b600c548110156108d257600d6000828152602001908152602001600020600101548201915080806001019150506108a0565b50600554811480156109075750600160038111156108ec57fe5b60008054906101000a900460ff16600381111561090557fe5b145b61095c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526025815260200180610fae6025913960400191505060405180910390fd5b8260076001018190555060026000806101000a81548160ff0219169083600381111561098457fe5b02179055506001915050919050565b6040518060800160405280838152602001600560ff16600a0a83028152602001600081526020016000815250600d6000600c548152602001908152602001600020600082015181600001556020820151816001015560408201518160020155606082015181600301559050506001600c600082825401925050819055505050565b600f6020528060005260406000206000915090508060000154908060010154908060020154905083565b60045481565b600e5481565b60105481565b60005b600e54811015610be65783600f6000838152602001908152602001600020600001541415610bd95760005b601054811015610bbc5783601160008381526020019081526020016000206001015413158015610ac35750601160008281526020019081526020016000206002015484125b15610baf576000600f60008481526020019081526020016000206002015414610b9257600060116000838152602001908152602001600020600401549050600f60008481526020019081526020016000206002015484036011600084815260200190815260200160002060040160008282540192505081905550610b8c6011600084815260200190815260200160002060000154826011600086815260200190815260200160002060040154036011600086815260200190815260200160002060030154610e59565b50610bae565b82600f6000848152602001908152602001600020600201819055505b5b8080600101915050610a7e565b5081600f6000838152602001908152602001600020600201819055505b8080600101915050610a53565b50505050565b6007806000015490806001015490806002015490806003015490806004018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c9e5780601f10610c7357610100808354040283529160200191610c9e565b820191906000526020600020905b815481529060010190602001808311610c8157829003601f168201915b5050505050905085565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600060019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600c5481565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60055481565b565b600080600090505b828160ff161015610d5557600a8481610d4557fe5b0493508080600101915050610d30565b50600a8381610d6057fe5b06905092915050565b600581565b60065481565b60005b600e54811015610e515784600f6000838152602001908152602001600020600101541415610e4457600086600a600f600085815260200190815260200160002060000154020190506040518060a00160405280828152602001868152602001858152602001848152602001600081525060116000601054815260200190815260200160002060008201518160000155602082015181600101556040820151816002015560608201518160030155608082015181600401559050506001601060008282540192505081905550505b8080600101915050610d77565b505050505050565b6000610e66846002610d28565b9050600060068190555060005b600c54811015610fa65781600d6000838152602001908152602001600020600001541415610f7357606483603c8681610ea857fe5b0402600d600084815260200190815260200160002060030154600d60008581526020019081526020016000206001015481610edf57fe5b040281610ee857fe5b04600d600083815260200190815260200160002060020160008282540192505081905550600d600082815260200190815260200160002060010154600d6000838152602001908152602001600020600201541115610f7257600d600082815260200190815260200160002060010154600d6000838152602001908152602001600020600201819055505b5b600d6000828152602001908152602001600020600201546006600082825401925050819055508080600101915050610e73565b505050505056fe41637469766174696f6e20526571756972656d656e7473204e6f7420436f6d706c65746564446561637469766174696f6e20526571756972656d656e7473204e6f7420436f6d706c65746564a2646970667358221220623f6b9d74ff2336aca565423a698b544af81671e32ac7525e810504bca065bb64736f6c63430007000033";

    public static final String FUNC_BROKER = "broker";

    public static final String FUNC_CLIENT = "client";

    public static final String FUNC_CONDITIONLEVELCOUNT = "conditionLevelCount";

    public static final String FUNC_CONDITIONLEVELS = "conditionLevels";

    public static final String FUNC_CONTRACTDECIMALS = "contractDecimals";

    public static final String FUNC_CONTRACTLIABILITY = "contractLiability";

    public static final String FUNC_CONTRACTPREMIUM = "contractPremium";

    public static final String FUNC_CONTRACTRESERVE = "contractReserve";

    public static final String FUNC_INSURANCE = "insurance";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PARAMETERS = "parameters";

    public static final String FUNC_SENSORCOUNT = "sensorCount";

    public static final String FUNC_SENSORS = "sensors";

    public static final String FUNC_SHIPMENTCOUNT = "shipmentCount";

    public static final String FUNC_SHIPMENTS = "shipments";

    public static final String FUNC_ACTIVATECONTRACT = "activateContract";

    public static final String FUNC_DEACTIVATECONTRACT = "deactivateContract";

    public static final String FUNC_FUNDCONTRACT = "fundContract";

    public static final String FUNC_ADDSHIPMENT = "addShipment";

    public static final String FUNC_ADDSENSOR = "addSensor";

    public static final String FUNC_ADDCONDITIONLEVEL = "addConditionLevel";

    public static final String FUNC_UPDATESENSOR = "updateSensor";

    public static final String FUNC_EXTRACTDIGIT = "extractDigit";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected SmartInsurancePolicy(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SmartInsurancePolicy(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SmartInsurancePolicy(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SmartInsurancePolicy(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> broker() {
        final Function function = new Function(FUNC_BROKER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> client() {
        final Function function = new Function(FUNC_CLIENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> conditionLevelCount() {
        final Function function = new Function(FUNC_CONDITIONLEVELCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>> conditionLevels(BigInteger param0) {
        final Function function = new Function(FUNC_CONDITIONLEVELS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Int256>() {}, new TypeReference<Int256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> contractDecimals() {
        final Function function = new Function(FUNC_CONTRACTDECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> contractLiability() {
        final Function function = new Function(FUNC_CONTRACTLIABILITY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> contractPremium() {
        final Function function = new Function(FUNC_CONTRACTPREMIUM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> contractReserve() {
        final Function function = new Function(FUNC_CONTRACTRESERVE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> insurance() {
        final Function function = new Function(FUNC_INSURANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, String>> parameters() {
        final Function function = new Function(FUNC_PARAMETERS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, String>>(function,
                new Callable<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, String>>() {
                    @Override
                    public Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, String>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (String) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> sensorCount() {
        final Function function = new Function(FUNC_SENSORCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, BigInteger>> sensors(BigInteger param0) {
        final Function function = new Function(FUNC_SENSORS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> shipmentCount() {
        final Function function = new Function(FUNC_SHIPMENTCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>> shipments(BigInteger param0) {
        final Function function = new Function(FUNC_SHIPMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> activateContract(BigInteger activationT) {
        final Function function = new Function(
                FUNC_ACTIVATECONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(activationT)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deactivateContract(BigInteger deactivationT) {
        final Function function = new Function(
                FUNC_DEACTIVATECONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(deactivationT)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> fundContract() {
        final Function function = new Function(
                FUNC_FUNDCONTRACT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addShipment(BigInteger sID, BigInteger sLiability) {
        final Function function = new Function(
                FUNC_ADDSHIPMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(sID), 
                new org.web3j.abi.datatypes.generated.Uint256(sLiability)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addSensor(BigInteger sID, BigInteger sType) {
        final Function function = new Function(
                FUNC_ADDSENSOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(sID), 
                new org.web3j.abi.datatypes.generated.Uint256(sType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addConditionLevel(BigInteger lDepth, BigInteger lType, BigInteger lMin, BigInteger lMax, BigInteger lWeight) {
        final Function function = new Function(
                FUNC_ADDCONDITIONLEVEL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(lDepth), 
                new org.web3j.abi.datatypes.generated.Uint256(lType), 
                new org.web3j.abi.datatypes.generated.Int256(lMin), 
                new org.web3j.abi.datatypes.generated.Int256(lMax), 
                new org.web3j.abi.datatypes.generated.Uint256(lWeight)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateSensor(BigInteger sensorID, BigInteger sensorData, BigInteger dataTimestamp) {
        final Function function = new Function(
                FUNC_UPDATESENSOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(sensorID), 
                new org.web3j.abi.datatypes.generated.Int256(sensorData), 
                new org.web3j.abi.datatypes.generated.Uint256(dataTimestamp)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> extractDigit(BigInteger number, BigInteger target) {
        final Function function = new Function(
                FUNC_EXTRACTDIGIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(number), 
                new org.web3j.abi.datatypes.generated.Uint256(target)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SmartInsurancePolicy load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartInsurancePolicy(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SmartInsurancePolicy load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartInsurancePolicy(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SmartInsurancePolicy load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SmartInsurancePolicy(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SmartInsurancePolicy load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SmartInsurancePolicy(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SmartInsurancePolicy> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _client, String _insurance, String _broker, BigInteger premium, BigInteger liability, BigInteger inception, BigInteger expiry, String scope) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_client), 
                new org.web3j.abi.datatypes.Address(_insurance), 
                new org.web3j.abi.datatypes.Address(_broker), 
                new org.web3j.abi.datatypes.generated.Uint256(premium), 
                new org.web3j.abi.datatypes.generated.Uint256(liability), 
                new org.web3j.abi.datatypes.generated.Uint256(inception), 
                new org.web3j.abi.datatypes.generated.Uint256(expiry), 
                new org.web3j.abi.datatypes.Utf8String(scope)));
        return deployRemoteCall(SmartInsurancePolicy.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<SmartInsurancePolicy> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _client, String _insurance, String _broker, BigInteger premium, BigInteger liability, BigInteger inception, BigInteger expiry, String scope) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_client), 
                new org.web3j.abi.datatypes.Address(_insurance), 
                new org.web3j.abi.datatypes.Address(_broker), 
                new org.web3j.abi.datatypes.generated.Uint256(premium), 
                new org.web3j.abi.datatypes.generated.Uint256(liability), 
                new org.web3j.abi.datatypes.generated.Uint256(inception), 
                new org.web3j.abi.datatypes.generated.Uint256(expiry), 
                new org.web3j.abi.datatypes.Utf8String(scope)));
        return deployRemoteCall(SmartInsurancePolicy.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SmartInsurancePolicy> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _client, String _insurance, String _broker, BigInteger premium, BigInteger liability, BigInteger inception, BigInteger expiry, String scope) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_client), 
                new org.web3j.abi.datatypes.Address(_insurance), 
                new org.web3j.abi.datatypes.Address(_broker), 
                new org.web3j.abi.datatypes.generated.Uint256(premium), 
                new org.web3j.abi.datatypes.generated.Uint256(liability), 
                new org.web3j.abi.datatypes.generated.Uint256(inception), 
                new org.web3j.abi.datatypes.generated.Uint256(expiry), 
                new org.web3j.abi.datatypes.Utf8String(scope)));
        return deployRemoteCall(SmartInsurancePolicy.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SmartInsurancePolicy> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _client, String _insurance, String _broker, BigInteger premium, BigInteger liability, BigInteger inception, BigInteger expiry, String scope) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_client), 
                new org.web3j.abi.datatypes.Address(_insurance), 
                new org.web3j.abi.datatypes.Address(_broker), 
                new org.web3j.abi.datatypes.generated.Uint256(premium), 
                new org.web3j.abi.datatypes.generated.Uint256(liability), 
                new org.web3j.abi.datatypes.generated.Uint256(inception), 
                new org.web3j.abi.datatypes.generated.Uint256(expiry), 
                new org.web3j.abi.datatypes.Utf8String(scope)));
        return deployRemoteCall(SmartInsurancePolicy.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
