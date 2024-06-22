package pl.masterthesis.security.jwt.model;

import lombok.Builder;

@Builder
public record TokenDTO(String accessToken, String refreshToken, String clientId) {
}
