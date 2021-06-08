package tss.orchestrator.service;

import tss.orchestrator.api.dto.SensorsDataDTO;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

public interface BlockChainService {
    BlockChainResponseTransfer createSmartPolicy(String privateKey, SmartPolicy smartPolicy);
    void updateSensorsData(SensorsDataDTO sensorsDataDTO);
    BlockChainResponseTransfer deactivate(SmartPolicy smartPolicy);
}
