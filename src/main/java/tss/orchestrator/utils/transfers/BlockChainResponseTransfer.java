package tss.orchestrator.utils.transfers;

import java.time.Duration;

public class BlockChainResponseTransfer {
    public BlockChainResponseTransfer() {}

    private Duration performance;
    private String message;

    public Duration getPerformance() {
        return performance;
    }

    public void setPerformance(Duration performance) {
        this.performance = performance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
