package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.entities.user.UserdataEntity;
import com.example.itbangmodkradankanbanapi.entities.userThirdParty.UserThirdParty;
import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedLoginException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("#{${jwt.max-token-interval-hour}*60*60*1000}")
    private long JWT_TOKEN_VALIDITY;
    @Value("#{${jwt.max-ref-token-interval-hour}*60*60*1000}")
    private long JWT_REF_TOKEN_VALIDITY;
    @Value("${jwt.access.token.cookie.name}")
    private String jwtCookie;
    @Value("${jwt.ref.access.token.cookie.name}")
    private String jwtRefCookie;
    @Value("${jwt.access.token.cookie.expired}")
    private String jwtCookieExpired;

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public ResponseCookie generateJwtCookie(UserdataEntity userPrincipal) {
        String jwt = generateToken(userPrincipal);
        return generateCookie(jwtCookie, jwt);
    }


    public ResponseCookie generateRefreshJwtCookie(UserdataEntity userPrincipal) {
        String jwt = generateRefreshToken(userPrincipal);
        return generateCookie(jwtRefCookie, jwt);
    }

    public ResponseCookie generateCookie(String name, String value) {
        return ResponseCookie
                .from(name, value).path("/").
                maxAge(Integer.parseInt(jwtCookieExpired))
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }

    public ResponseCookie generateCookieThirdParty(UserThirdParty userThirdParty) {
        String jwt = generateTokenThirdParty(userThirdParty);
        return generateCookie(jwtCookie, jwt);
    }

    public ResponseCookie generateRefreshCookieThirdParty(UserThirdParty userThirdParty) {
        String jwt = generateRefreshTokenThirdParty(userThirdParty);
        return generateCookie(jwtRefCookie, jwt);
    }

    public ResponseCookie removeCookie(String name) {
        return ResponseCookie
                .from(name, "").path("/").
                maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }


    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error decoding JWT token");
        }
    }


    public String generateToken(UserdataEntity userdataEntity) {
        return doGenerateToken(setClaims(userdataEntity), userdataEntity.getUsername());
    }

    private Map<String, Object> setClaims(UserdataEntity userdataEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", userdataEntity.getOid());
        claims.put("email", userdataEntity.getEmail());
        claims.put("role", userdataEntity.getRole());
        claims.put("name", userdataEntity.getName());
        return claims;
    }

    private Map<String, Object> setClaimsThirdParty(UserThirdParty userThirdParty) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", userThirdParty.getOid());
        claims.put("email", userThirdParty.getEmail());
        claims.put("platform", userThirdParty.getPlatform());
        claims.put("name", userThirdParty.getName());
        return claims;
    }

    public String generateTokenThirdParty(UserThirdParty userThirdParty) {
        return doGenerateToken(setClaimsThirdParty(userThirdParty), userThirdParty.getName());
    }

    public String generateRefreshTokenThirdParty(UserThirdParty userThirdParty) {
        return doGenerateRefreshToken(setClaimsThirdParty(userThirdParty), userThirdParty.getName());
    }

    public String generateRefreshToken(UserdataEntity userdataEntity) {
        return doGenerateRefreshToken(setClaims(userdataEntity), userdataEntity.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("https://intproj23.sit.kmutt.ac.th/at3/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY).compact();
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(subject)
                .setClaims(claims)
                .setIssuer("https://intproj23.sit.kmutt.ac.th/at3/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REF_TOKEN_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY).compact();
    }


    public boolean validateToken(String authToken) {
        if (authToken == null) throw new UnauthorizedLoginException("Must have JWT refresh token");
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw new UnauthorizedLoginException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedLoginException("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedLoginException("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedLoginException("JWT claims string is empty: " + e.getMessage());
        } catch (Exception e) {
            throw new UnauthorizedLoginException("JWT claims string is Invalid: " + e.getMessage());
        }

    }

    public String getTokenCookie(Cookie[] cookies) {
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtCookie.equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }
        return jwtToken;
    }

    public String getRefTokenCookie(Cookie[] cookies) {
        String jwtRefToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtRefCookie.equals(cookie.getName())) {
                    jwtRefToken = cookie.getValue();
                    break;
                }
            }
        }
        return jwtRefToken;
    }
}
