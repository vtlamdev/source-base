package com.vtlamdev.sourcebase.service.security.model.token;

import com.vtlamdev.sourcebase.common.data.id.CustomerId;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import com.vtlamdev.sourcebase.common.data.security.Authority;
import com.vtlamdev.sourcebase.common.data.security.model.JwtPair;
import com.vtlamdev.sourcebase.common.data.security.model.JwtToken;
import com.vtlamdev.sourcebase.security.SourceBaseSecurityProperties;
import com.vtlamdev.sourcebase.service.security.model.SecurityUser;
import com.vtlamdev.sourcebase.service.security.model.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtTokenFactory {

    private static final String SCOPES = "scopes";
    private static final String USER_ID = "userId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ENABLED = "enabled";
    private static final String TENANT_ID = "tenantId";
    private static final String CUSTOMER_ID = "customerId";
    private static final String SESSION_ID = "sessionId";
    private static final String TOKEN_TYPE = "tokenType";

    private final SourceBaseSecurityProperties securityProperties;

    public JwtTokenFactory(SourceBaseSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public JwtPair createTokenPair(SecurityUser securityUser) {
        securityUser.setSessionId(UUID.randomUUID().toString());
        JwtToken accessToken = createAccessJwtToken(securityUser);
        JwtToken refreshToken = createRefreshToken(securityUser);
        return new JwtPair(accessToken.token(), refreshToken.token());
    }

    public AccessJwtToken createAccessJwtToken(SecurityUser securityUser) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(securityUser.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(securityProperties.getJwt().getTokenExpirationTime())))
                .issuer(securityProperties.getJwt().getTokenIssuer())
                .claim(USER_ID, securityUser.getId().getId().toString())
                .claim(SCOPES, securityUser.getAuthorities().stream().map(granted -> granted.getAuthority()).toList())
                .claim(FIRST_NAME, securityUser.getFirstName())
                .claim(LAST_NAME, securityUser.getLastName())
                .claim(ENABLED, securityUser.isEnabled())
                .claim(TOKEN_TYPE, "access")
                .claim(SESSION_ID, securityUser.getSessionId())
                .claim(TENANT_ID, securityUser.getTenantId() != null ? securityUser.getTenantId().getId().toString() : null)
                .claim(CUSTOMER_ID, securityUser.getCustomerId() != null ? securityUser.getCustomerId().getId().toString() : null)
                .signWith(getSecretKey())
                .compact();
        return new AccessJwtToken(token);
    }

    public JwtToken createRefreshToken(SecurityUser securityUser) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(securityUser.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(securityProperties.getJwt().getRefreshTokenExpTime())))
                .issuer(securityProperties.getJwt().getTokenIssuer())
                .claim(USER_ID, securityUser.getId().getId().toString())
                .claim(SCOPES, List.of(Authority.REFRESH_TOKEN.name()))
                .claim(TOKEN_TYPE, "refresh")
                .claim(SESSION_ID, securityUser.getSessionId())
                .signWith(getSecretKey())
                .compact();
        return new AccessJwtToken(token);
    }

    public SecurityUser parseAccessJwtToken(String token) {
        Claims claims = parseClaims(token);
        validateTokenType(claims, "access");
        return toSecurityUser(claims, true);
    }

    public SecurityUser parseRefreshToken(String token) {
        Claims claims = parseClaims(token);
        validateTokenType(claims, "refresh");
        return toSecurityUser(claims, false);
    }

    private SecurityUser toSecurityUser(Claims claims, boolean fullUser) {
        SecurityUser securityUser = new SecurityUser(new UserId(UUID.fromString(claims.get(USER_ID, String.class))));
        securityUser.setEmail(claims.getSubject());
        if (fullUser) {
            List<String> scopes = claims.get(SCOPES, List.class);
            if (scopes == null || scopes.isEmpty()) {
                throw new BadCredentialsException("JWT token has no scopes");
            }
            securityUser.setAuthority(Authority.parse(scopes.get(0)));
            securityUser.setFirstName(claims.get(FIRST_NAME, String.class));
            securityUser.setLastName(claims.get(LAST_NAME, String.class));
            Boolean enabled = claims.get(ENABLED, Boolean.class);
            securityUser.setEnabled(Boolean.TRUE.equals(enabled));
            String tenantId = claims.get(TENANT_ID, String.class);
            if (tenantId != null) {
                securityUser.setTenantId(TenantId.fromUUID(UUID.fromString(tenantId)));
            }
            String customerId = claims.get(CUSTOMER_ID, String.class);
            if (customerId != null) {
                securityUser.setCustomerId(new CustomerId(UUID.fromString(customerId)));
            }
        }
        securityUser.setSessionId(claims.get(SESSION_ID, String.class));
        securityUser.setUserPrincipal(new UserPrincipal(UserPrincipal.Type.USER_NAME, claims.getSubject()));
        return securityUser;
    }

    private Claims parseClaims(String token) {
        try {
            Jws<Claims> jws = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
            return jws.getPayload();
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid JWT token", e);
        }
    }

    private void validateTokenType(Claims claims, String expectedType) {
        String tokenType = claims.get(TOKEN_TYPE, String.class);
        if (!expectedType.equals(tokenType)) {
            throw new BadCredentialsException("Invalid JWT token type");
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(securityProperties.getJwt().getTokenSigningKey()));
    }

}
