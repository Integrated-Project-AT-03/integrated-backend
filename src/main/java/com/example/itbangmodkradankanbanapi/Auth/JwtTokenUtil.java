package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedLoginException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.SignatureException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

    private ResponseCookie generateCookie(String name, String value) {
        return  ResponseCookie
                .from(name, value).path("/").
                maxAge(Integer.parseInt(jwtCookieExpired))
                .httpOnly(true)
                .secure(true)
                .sameSite("None").build();
    }

    public ResponseCookie removeCookie(String name) {
        return  ResponseCookie
                .from(name, "").path("/").
                maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None").build();
    }


//    public String getUsernameFromToken(String token) {
//        return getClaimFromToken(token, Claims::getSubject);
//    }

//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//    }

//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }

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
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid",userdataEntity.getOid());
        claims.put("email",userdataEntity.getEmail());
        claims.put("role",userdataEntity.getRole());
        claims.put("name", userdataEntity.getName());
        return doGenerateToken(claims, userdataEntity.getUsername());
    }

    public String generateRefreshToken(UserdataEntity userdataEntity) {
        return doGenerateRefreshToken( userdataEntity.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims).setSubject(subject)
                .setIssuer("https://intproj23.sit.kmutt.ac.th/at3/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY).compact();
    }

    private String doGenerateRefreshToken(String subject) {
        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(subject)
                .setIssuer("https://intproj23.sit.kmutt.ac.th/at3/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REF_TOKEN_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY).compact();
    }
//    public Boolean validateToken(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(token)
//                    .getBody();
//            return (!claims.getExpiration().before(new Date()));
//
//        } catch (JwtException e) {
//            System.out.println("Invalid JWT token: " + e.getMessage());
//            return false;
//        } catch (Exception e) {
//            System.out.println("Invalid JWT token");
//            return false;
//        }
//    }

    public boolean validateToken(String authToken) {
        if (authToken == null) throw  new UnauthorizedLoginException("Must have JWT refresh token");
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw  new UnauthorizedLoginException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw  new UnauthorizedLoginException("JWT token is expired: "+ e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw  new UnauthorizedLoginException("JWT token is unsupported: "+ e.getMessage());
        } catch (IllegalArgumentException e) {
            throw  new UnauthorizedLoginException("JWT claims string is empty: "+ e.getMessage());
        } catch (Exception e){
            throw  new UnauthorizedLoginException("JWT claims string is Invalid: "+ e.getMessage());
        }

    }

    public String getTokenCookie(Cookie[] cookies){
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtCookie.equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }
        return  jwtToken;
    }

    public String getRefTokenCookie(Cookie[] cookies){
        String jwtRefToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtRefCookie.equals(cookie.getName())) {
                    jwtRefToken = cookie.getValue();
                    break;
                }
            }
        }
        return  jwtRefToken;
    }


    public Boolean isTokenExpired(String token) {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
    }
}