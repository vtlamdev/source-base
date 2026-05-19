package com.vtlamdev.sourcebase.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "source-base.security")
public class SourceBaseSecurityProperties {

    private boolean permitAll = true;
    private Jwt jwt = new Jwt();

    public boolean isPermitAll() {
        return permitAll;
    }

    public void setPermitAll(boolean permitAll) {
        this.permitAll = permitAll;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public static class Jwt {

        private Integer tokenExpirationTime = 900;
        private Integer refreshTokenExpTime = 604800;
        private String tokenIssuer = "source-base";
        private String tokenSigningKey;

        public Integer getTokenExpirationTime() {
            return tokenExpirationTime;
        }

        public void setTokenExpirationTime(Integer tokenExpirationTime) {
            this.tokenExpirationTime = tokenExpirationTime;
        }

        public Integer getRefreshTokenExpTime() {
            return refreshTokenExpTime;
        }

        public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
            this.refreshTokenExpTime = refreshTokenExpTime;
        }

        public String getTokenIssuer() {
            return tokenIssuer;
        }

        public void setTokenIssuer(String tokenIssuer) {
            this.tokenIssuer = tokenIssuer;
        }

        public String getTokenSigningKey() {
            return tokenSigningKey;
        }

        public void setTokenSigningKey(String tokenSigningKey) {
            this.tokenSigningKey = tokenSigningKey;
        }

    }

}
