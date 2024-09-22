package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    @Value("${jwt.access.token.cookie.name}")
    private String jwtCookie;
    @Value("${jwt.access.token.cookie.expired}")
    private String jwtCookieExpired;

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public ResponseCookie generateJwtCookie(UserdataEntity userPrincipal) {
        String jwt = generateToken(userPrincipal);
        return generateCookie(jwtCookie, jwt);
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
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims).setSubject(subject)
                .setIssuer("https://intproj23.sit.kmutt.ac.th/at3/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY).compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            final String username = claims.getSubject();
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

        } catch (JwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Invalid JWT token");
            return false;
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


    public Boolean isTokenExpired(String token) {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
    }
}