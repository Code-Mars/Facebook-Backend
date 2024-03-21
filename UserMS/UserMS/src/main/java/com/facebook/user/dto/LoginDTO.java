package com.facebook.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LoginDTO {
    @NotNull(message = "{user.username.absent}")
    @Pattern(regexp = "[A-Z][a-z]*[A-Z]?[a-z0-9]*", message = "{user.username.invalid}")
    private String username;
    private String email;
    @NotNull(message="{user.password.absent}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message="{user.password.invalid}")
    private String password;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    

}
