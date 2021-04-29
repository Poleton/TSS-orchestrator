package tss.orchestrator.models.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class Insurance_policy extends Contract {
    public static final String BINARY = "0x608060405260008055600060025534801561001957600080fd5b506040516109283803806109288339818101604052606081101561003c57600080fd5b8101908080519060200190929190805190602001909291908051604051939291908464010000000082111561007057600080fd5b8382019150602082018581111561008657600080fd5b82518660018202830111640100000000821117156100a357600080fd5b8083526020830192505050908051906020019080838360005b838110156100d75780820151818401526020810190506100bc565b50505050905090810190601f1680156101045780820380516001836020036101000a031916815260200191505b506040525050506040518060a00160405280848152602001600081526020018381526020016000815260200182815250600460008201518160000155602082015181600101556040820151816002015560608201518160030155608082015181600401908051906020019061017a929190610186565b50905050505050610223565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106101c757805160ff19168380011785556101f5565b828001600101855582156101f5579182015b828111156101f45782518255916020019190600101906101d9565b5b5090506102029190610206565b5090565b5b8082111561021f576000816000905550600101610207565b5090565b6106f6806102326000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c8063651a2b4c1161005b578063651a2b4c146101b6578063813f1d1d146101d45780638903573014610216578063d0fe3cc8146102b557610088565b8063087f85561461008d57806309dfcd00146100e3578063412f2e8214610141578063642cd78714610198575b600080fd5b6100e1600480360360a08110156100a357600080fd5b8101908080359060200190929190803590602001909291908035906020019092919080359060200190929190803590602001909291905050506102f7565b005b61010f600480360360208110156100f957600080fd5b81019080803590602001909291905050506103da565b604051808681526020018581526020018481526020018381526020018281526020019550505050505060405180910390f35b61016d6004803603602081101561015757600080fd5b8101908080359060200190929190505050610410565b6040518085815260200184815260200183815260200182815260200194505050505060405180910390f35b6101a0610440565b6040518082815260200191505060405180910390f35b6101be610446565b6040518082815260200191505060405180910390f35b610214600480360360608110156101ea57600080fd5b8101908080359060200190929190803590602001909291908035906020019092919050505061044c565b005b61021e61058c565b6040518086815260200185815260200184815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561027657808201518184015260208101905061025b565b50505050905090810190601f1680156102a35780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b6102f5600480360360608110156102cb57600080fd5b81019080803590602001909291908035906020019092919080359060200190929190505050610648565b005b60005b6002548110156103d25784600360008381526020019081526020016000206001015414156103c557600086600a6003600085815260200190815260200160002060000154020190506040518060a0016040528082815260200186815260200185815260200184815260200160008152506001600080548152602001908152602001600020600082015181600001556020820151816001015560408201518160020155606082015181600301556080820151816004015590505060016000808282540192505081905550505b80806001019150506102fa565b505050505050565b60016020528060005260406000206000915090508060000154908060010154908060020154908060030154908060040154905085565b60036020528060005260406000206000915090508060000154908060010154908060020154908060030154905084565b60025481565b60005481565b60005b6002548110156105865783600360008381526020019081526020016000206000015414156105795760005b60005481101561055c576001600082815260200190815260200160002060020154841080156104be5750600160008281526020019081526020016000206001015484115b1561054f576000600360008481526020019081526020016000206003015414156105025782600360008481526020019081526020016000206003018190555061054e565b6003600083815260200190815260200160002060030154830360016000838152602001908152602001600020600401540160016000838152602001908152602001600020600401819055505b5b808060010191505061047a565b508160036000838152602001908152602001600020600301819055505b808060010191505061044f565b50505050565b6004806000015490806001015490806002015490806003015490806004018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561063e5780601f106106135761010080835404028352916020019161063e565b820191906000526020600020905b81548152906001019060200180831161062157829003601f168201915b5050505050905085565b6040518060800160405280848152602001838152602001828152602001600081525060036000600254815260200190815260200160002060008201518160000155602082015181600101556040820151816002015560608201518160030155905050600160026000828254019250508190555050505056fea26469706673582212204a4501457d169b7dfb4215da8ea4c3963a4a7814c548f3fc63110aa4d21be56664736f6c63430007000033";

    public static final String FUNC_CONDITIONLEVELS = "conditionLevels";

    public static final String FUNC_LEVELCOUNT = "levelCount";

    public static final String FUNC_PARAMETERS = "parameters";

    public static final String FUNC_SENSORCOUNT = "sensorCount";

    public static final String FUNC_SENSORS = "sensors";

    public static final String FUNC_ADDSENSOR = "addSensor";

    public static final String FUNC_ADDLEVEL = "addLevel";

    public static final String FUNC_UPDATESENSOR = "updateSensor";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected Insurance_policy(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Insurance_policy(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Insurance_policy(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Insurance_policy(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>> conditionLevels(BigInteger param0) {
        final Function function = new Function(FUNC_CONDITIONLEVELS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
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

    public RemoteFunctionCall<BigInteger> levelCount() {
        final Function function = new Function(FUNC_LEVELCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>> sensors(BigInteger param0) {
        final Function function = new Function(FUNC_SENSORS, 
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

    public RemoteFunctionCall<TransactionReceipt> addSensor(BigInteger sID, BigInteger sType, BigInteger sUpdatePeriodicity) {
        final Function function = new Function(
                FUNC_ADDSENSOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(sID), 
                new org.web3j.abi.datatypes.generated.Uint256(sType), 
                new org.web3j.abi.datatypes.generated.Uint256(sUpdatePeriodicity)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addLevel(BigInteger lDepth, BigInteger lType, BigInteger lMin, BigInteger lMax, BigInteger lWeight) {
        final Function function = new Function(
                FUNC_ADDLEVEL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(lDepth), 
                new org.web3j.abi.datatypes.generated.Uint256(lType), 
                new org.web3j.abi.datatypes.generated.Uint256(lMin), 
                new org.web3j.abi.datatypes.generated.Uint256(lMax), 
                new org.web3j.abi.datatypes.generated.Uint256(lWeight)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateSensor(BigInteger sensorID, BigInteger sensorData, BigInteger newDataTime) {
        final Function function = new Function(
                FUNC_UPDATESENSOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(sensorID), 
                new org.web3j.abi.datatypes.generated.Uint256(sensorData), 
                new org.web3j.abi.datatypes.generated.Uint256(newDataTime)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Insurance_policy load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Insurance_policy(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Insurance_policy load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Insurance_policy(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Insurance_policy load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Insurance_policy(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Insurance_policy load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Insurance_policy(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Insurance_policy> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger inception, BigInteger expiry, String scope) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(inception), 
                new org.web3j.abi.datatypes.generated.Uint256(expiry), 
                new org.web3j.abi.datatypes.Utf8String(scope)));
        return deployRemoteCall(Insurance_policy.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Insurance_policy> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger inception, BigInteger expiry, String scope) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(inception), 
                new org.web3j.abi.datatypes.generated.Uint256(expiry), 
                new org.web3j.abi.datatypes.Utf8String(scope)));
        return deployRemoteCall(Insurance_policy.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Insurance_policy> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger inception, BigInteger expiry, String scope) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(inception), 
                new org.web3j.abi.datatypes.generated.Uint256(expiry), 
                new org.web3j.abi.datatypes.Utf8String(scope)));
        return deployRemoteCall(Insurance_policy.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Insurance_policy> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger inception, BigInteger expiry, String scope) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(inception), 
                new org.web3j.abi.datatypes.generated.Uint256(expiry), 
                new org.web3j.abi.datatypes.Utf8String(scope)));
        return deployRemoteCall(Insurance_policy.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
