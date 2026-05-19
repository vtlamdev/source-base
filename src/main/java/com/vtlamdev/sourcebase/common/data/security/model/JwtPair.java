package com.vtlamdev.sourcebase.common.data.security.model;

import com.vtlamdev.sourcebase.common.data.security.Authority;

public class JwtPair {

    private String token;
    private String refreshToken;
    private Authority scope;

    public JwtPair() {
    }

    public JwtPair(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Authority getScope() {
        return scope;
    }

    public void setScope(Authority scope) {
        this.scope = scope;
    }

}
