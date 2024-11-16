package com.example.itbangmodkradankanbanapi.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


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

    @GetMapping("/callback")
    public ResponseEntity<Object> handleCallback(@RequestParam("code") String authorizationCode) {
        try {
            String accessToken = authenticationMASLService.exchangeAuthorizationCodeForToken(authorizationCode);
            return ResponseEntity.ok(accessToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exchanging authorization code: " + e.getMessage());
        }
    }


}
