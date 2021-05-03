package tss.orchestrator.service;

import tss.orchestrator.api.dto.SensorsDataDTO;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

public interface BlockChainService {
    void initialize(String privateKey);
    BlockChainResponseTransfer deployContract (SmartPolicy smartPolicy);
    BlockChainResponseTransfer sendSensorsData (SmartPolicy smartPolicy, SensorsDataDTO sensorsDataDTO);
}
