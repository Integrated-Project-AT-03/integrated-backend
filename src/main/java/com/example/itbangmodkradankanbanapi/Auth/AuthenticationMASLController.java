package com.example.itbangmodkradankanbanapi.Auth;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/auth/misl")
public class AuthenticationMASLController {
    @Autowired
    AuthenticationMASLService authenticationMASLService;

    @GetMapping("/login")
    public String login() {
        return authenticationMASLService.login();
    }

    @Value("${value.web.redirect}")
    private String webRedirectUri;

    @GetMapping("/callback")
    public void handleCallback(@RequestParam("code") String authorizationCode, HttpServletResponse response) {
        try {

            ResponseEntity<Object> result = authenticationMASLService.exchangeAuthorizationCodeForToken(authorizationCode);


            String accessTokenCookie = result.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            String refreshTokenCookie = result.getHeaders().get(HttpHeaders.SET_COOKIE).get(1);

            response.addHeader("Set-Cookie", accessTokenCookie);
            response.addHeader("Set-Cookie", refreshTokenCookie);

            response.sendRedirect(webRedirectUri);

        } catch (Exception e) {
            try {
                response.sendRedirect("http://localhost:4173/error?message=" + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


}
