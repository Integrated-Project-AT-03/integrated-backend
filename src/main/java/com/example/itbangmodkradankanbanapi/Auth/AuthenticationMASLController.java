package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.exceptions.ConflictException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;


@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/auth/misl")
public class AuthenticationMASLController {

    @Value("${jwt.access.token.cookie.name}")
    private String jwtCookie;
    @Value("${jwt.ref.access.token.cookie.name}")
    private String jwtRefCookie;
    @Autowired
    AuthenticationMASLService authenticationMASLService;

    @Value("${microsoft.login.redirect.uri}")
    private String webRedirectUri;

    @GetMapping("/login")
    public String login() {
        return authenticationMASLService.login();
    }


    @GetMapping("/callback/logout")
    public void logoutCallback(HttpServletResponse response)  {
        try {
        Map<String,String> result = authenticationMASLService.logoutCallback();
        response.addHeader("Set-Cookie",result.get(jwtCookie));
        response.addHeader("Set-Cookie", result.get(jwtRefCookie));
        response.addHeader("Set-Cookie", result.get("micJwtAccessToken"));
        response.sendRedirect(webRedirectUri+"/login");
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return authenticationMASLService.logout();
    }

    @GetMapping("/callback/login")
    public void handleCallback(@RequestParam("code") String authorizationCode, HttpServletResponse response) {
        try {

            Map<String,String> result = authenticationMASLService.exchangeAuthorizationCodeForToken(authorizationCode);


            response.addHeader("Set-Cookie", result.get(jwtCookie));
            response.addHeader("Set-Cookie", result.get(jwtRefCookie));
            response.addHeader("Set-Cookie", result.get("micJwtAccessToken"));
            response.sendRedirect(webRedirectUri);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }


}
