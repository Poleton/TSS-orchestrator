package tss.orchestrator.api.dto;

public class PolicyDTO {
    private Integer id;
    private String state;
    private String holderName;
    //private String holderCIF;
    //private String product;
    //private Date expeditionDate;
    //private Date expirationDate;
    //private String vehicle;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

}
