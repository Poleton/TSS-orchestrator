package tss.orchestrator.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SensorsDataDTO {
    private int userId;
    private String contractAddress;
    private List<Integer> data;


}
