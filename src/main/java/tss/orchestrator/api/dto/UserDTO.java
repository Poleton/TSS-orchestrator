package tss.orchestrator.api.dto;

public class UserDTO {
    private String name;
    private String password;

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
}
