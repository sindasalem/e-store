package com.quest.etna.helpers;

import com.quest.etna.model.UserRole;

public class UserRequestBody {
    private String username;
    private String password;
    private UserRole role;

    public UserRequestBody() {

    }

    public UserRequestBody(String username, String password, UserRole role) {
        this.setUsername(username);
        this.setPassword(password);
        this.setRole(role);
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRequestBody{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
