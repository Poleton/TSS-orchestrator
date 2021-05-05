package tss.orchestrator.utils.transfers;

import lombok.Getter;
import lombok.Setter;
import tss.orchestrator.utils.constants.Constants;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class BlockChainResponseTransfer {

    private String contractAddress;
    private Constants.ContractState state;
    private BigInteger levelId;
    private BigInteger sensorType;
    private BigInteger sensorData;
    private BigInteger dataExcess;
    private BigInteger levelExcessTime;
    private BigInteger contractReserve;

}
