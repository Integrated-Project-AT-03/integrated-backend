package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.exceptions.ConflictException;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedLoginException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.rmi.ServerException;
import java.sql.Timestamp;
import java.time.Instant;
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
    public void login(HttpServletResponse response) throws IOException {
            response.sendRedirect(authenticationMASLService.login());
    }


    @GetMapping("/callback/logout")
    public void logoutCallback(HttpServletResponse response) throws IOException {

        Map<String,String> result = authenticationMASLService.logoutCallback();
        response.addHeader("Set-Cookie",result.get(jwtCookie));
        response.addHeader("Set-Cookie", result.get(jwtRefCookie));
        response.addHeader("Set-Cookie", result.get("micJwtAccessToken"));
        response.sendRedirect(webRedirectUri+"/login");
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws IOException {
        response.sendRedirect(authenticationMASLService.logout());
    }

    @GetMapping("/callback/login")
    public void handleCallback(@NotNull @RequestParam("code") String authorizationCode, HttpServletResponse response) throws IOException {
            Map<String,String> result = authenticationMASLService.exchangeAuthorizationCodeForToken(authorizationCode);
            response.addHeader("Set-Cookie", result.get(jwtCookie));
            response.addHeader("Set-Cookie", result.get(jwtRefCookie));
            response.addHeader("Set-Cookie", result.get("micJwtAccessToken"));
            response.sendRedirect(webRedirectUri);
    }

    @ExceptionHandler(UnauthorizedLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> UnauthorizedLoginException(UnauthorizedLoginException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.UNAUTHORIZED.value(), null,ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
    }


}
