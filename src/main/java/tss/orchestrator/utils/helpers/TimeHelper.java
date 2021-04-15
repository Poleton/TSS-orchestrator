package tss.orchestrator.utils.helpers;

import java.time.Duration;
import java.time.Instant;

public class TimeHelper {

    public static Instant start() {
        return Instant.now();
    }

    public static Duration stop(Instant start) {
        Instant end = Instant.now();
        return Duration.between(start, end);
    }
}
