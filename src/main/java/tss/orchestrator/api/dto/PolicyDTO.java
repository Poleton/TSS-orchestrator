package tss.orchestrator.api.dto;


public class PolicyDTO {

    private String holderName;
    private String product;
    private String duration;
    private String territorialScope;
    private String meansOfTransport;
    private int numSensors;
    private String conditions;





    public String getHolderName() {
        return this.holderName;
    }

    public void setHolderName(String name) {
        this.holderName = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTerritorialScope() {
        return territorialScope;
    }

    public void setTerritorialScope(String territorialScope) {
        this.territorialScope = territorialScope;
    }

    public String getMeansOfTransport() {
        return meansOfTransport;
    }

    public void setMeansOfTransport(String meansOfTransport) {
        this.meansOfTransport = meansOfTransport;
    }

    public int getNumSensors() {
        return numSensors;
    }

    public void setNumSensors(int numSensors) {
        this.numSensors = numSensors;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
