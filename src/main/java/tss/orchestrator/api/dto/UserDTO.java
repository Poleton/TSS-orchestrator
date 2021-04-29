package tss.orchestrator.api.dto;

import javax.persistence.Column;

public class UserDTO {
    private String name;
    private String password;
    private String privateKey;

    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }

    public void setUsername(String username) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKey() {
        return privateKey;
    }
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
