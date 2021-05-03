package tss.orchestrator.utils.transfers;

import lombok.Getter;
import lombok.Setter;
import tss.orchestrator.utils.constants.Constants;

@Getter @Setter
public class BlockChainResponseTransfer {
    public BlockChainResponseTransfer() {}

    private String contractAddress;
    private Constants.ContractState state;

}
