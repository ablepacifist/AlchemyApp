package alchemy.srsys.object.DTO;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // getters (and setters if you need them)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
