package com.quest.etna.helpers;

public class AuthenticateRequestBody {
    private String username;
    private String password;

    public AuthenticateRequestBody() {

    }

    public AuthenticateRequestBody(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
