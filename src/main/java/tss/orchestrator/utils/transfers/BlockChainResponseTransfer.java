package tss.orchestrator.utils.transfers;

import lombok.Getter;
import lombok.Setter;
import tss.orchestrator.utils.constants.Constants;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class BlockChainResponseTransfer {

    private String contractAddress;
    private Constants.ContractState state;
    private Map<Constants.SensorType, Map<String, BigInteger>> events;

}
