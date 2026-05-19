package com.vtlamdev.sourcebase.service.security.model.token;

import com.vtlamdev.sourcebase.common.data.security.model.JwtToken;

public record AccessJwtToken(String token) implements JwtToken {
}
