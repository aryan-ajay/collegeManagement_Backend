package com.avit.collegemanagementsystem.security.jwt;

public class JwtResponse {
    private boolean valid;
    private String message;

    public JwtResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}

