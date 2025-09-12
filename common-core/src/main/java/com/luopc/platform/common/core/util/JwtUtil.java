package com.luopc.platform.common.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Utility
 *
 * @author Platform Team
 * @since 1.0.0
 */
@Slf4j
public class JwtUtil {

    private static final String DEFAULT_SECRET = "PlatformJWTSecret123456789012345678901234567890";
    private static final String ISSUER = "platform-system";
    private static final long DEFAULT_EXPIRE_TIME = 7200; // 2 hours in seconds

    // JWT Claims
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_TENANT_ID = "tenantId";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_PERMISSIONS = "permissions";

    /**
     * Generate JWT token with default expiration
     */
    public static String generateToken(String userId, String username) {
        return generateToken(userId, username, DEFAULT_EXPIRE_TIME);
    }

    /**
     * Generate JWT token with custom expiration
     */
    public static String generateToken(String userId, String username, long expireSeconds) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userId);
        claims.put(CLAIM_USERNAME, username);
        return generateToken(claims, expireSeconds);
    }

    /**
     * Generate JWT token with claims
     */
    public static String generateToken(Map<String, Object> claims, long expireSeconds) {
        return generateToken(claims, expireSeconds, DEFAULT_SECRET);
    }

    /**
     * Generate JWT token with custom secret
     */
    public static String generateToken(Map<String, Object> claims, long expireSeconds, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Date expiresAt = Date.from(Instant.now().plusSeconds(expireSeconds));

            var builder = JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(new Date())
                    .withExpiresAt(expiresAt);

            // Add claims
            claims.forEach((key, value) -> {
                if (value instanceof String) {
                    builder.withClaim(key, (String) value);
                } else if (value instanceof Integer) {
                    builder.withClaim(key, (Integer) value);
                } else if (value instanceof Long) {
                    builder.withClaim(key, (Long) value);
                } else if (value instanceof Boolean) {
                    builder.withClaim(key, (Boolean) value);
                } else if (value instanceof Date) {
                    builder.withClaim(key, (Date) value);
                } else {
                    builder.withClaim(key, value.toString());
                }
            });

            return builder.sign(algorithm);
        } catch (JWTCreationException e) {
            log.error("Failed to generate JWT token", e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    /**
     * Verify and decode JWT token
     */
    public static DecodedJWT verifyToken(String token) {
        return verifyToken(token, DEFAULT_SECRET);
    }

    /**
     * Verify and decode JWT token with custom secret
     */
    public static DecodedJWT verifyToken(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            log.error("JWT token verification failed", e);
            throw new RuntimeException("JWT token verification failed", e);
        }
    }

    /**
     * Decode JWT token without verification
     */
    public static DecodedJWT decodeToken(String token) {
        try {
            return JWT.decode(token);
        } catch (JWTDecodeException e) {
            log.error("Failed to decode JWT token", e);
            throw new RuntimeException("Failed to decode JWT token", e);
        }
    }

    /**
     * Get user ID from token
     */
    public static String getUserId(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim(CLAIM_USER_ID).asString();
    }

    /**
     * Get username from token
     */
    public static String getUsername(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim(CLAIM_USERNAME).asString();
    }

    /**
     * Get tenant ID from token
     */
    public static String getTenantId(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim(CLAIM_TENANT_ID).asString();
    }

    /**
     * Check if token is expired
     */
    public static boolean isTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = decodeToken(token);
            Date expiresAt = decodedJWT.getExpiresAt();
            return expiresAt.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Get token expiration time
     */
    public static LocalDateTime getExpirationTime(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        Date expiresAt = decodedJWT.getExpiresAt();
        return LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Get token issued time
     */
    public static LocalDateTime getIssuedTime(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        Date issuedAt = decodedJWT.getIssuedAt();
        return LocalDateTime.ofInstant(issuedAt.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Refresh token (generate new token with same claims but extended expiration)
     */
    public static String refreshToken(String token) {
        return refreshToken(token, DEFAULT_EXPIRE_TIME);
    }

    /**
     * Refresh token with custom expiration
     */
    public static String refreshToken(String token, long expireSeconds) {
        DecodedJWT decodedJWT = verifyToken(token);

        Map<String, Object> claims = new HashMap<>();
        decodedJWT.getClaims().forEach((key, claim) -> {
            if (!key.equals("iss") && !key.equals("iat") && !key.equals("exp")) {
                claims.put(key, claim.asString());
            }
        });

        return generateToken(claims, expireSeconds);
    }

    private JwtUtil() {
        // Utility class
    }
}
