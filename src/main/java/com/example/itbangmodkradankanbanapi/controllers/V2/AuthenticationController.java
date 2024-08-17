package com.example.itbangmodkradankanbanapi.controllers.V2;

import com.example.itbangmodkradankanbanapi.Auth.JwtRequestUser;
import com.example.itbangmodkradankanbanapi.Auth.JwtTokenUtil;
import com.example.itbangmodkradankanbanapi.Auth.JwtUserDetailsService;
import com.example.itbangmodkradankanbanapi.dtos.V2.JwtDtoV2;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedLoginException;
import com.example.itbangmodkradankanbanapi.user.UserDataRepository;
import com.example.itbangmodkradankanbanapi.user.UserdataEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;

@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/authentications")
public class AuthenticationController {
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDataRepository userDataRepository;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid JwtRequestUser jwtRequestUser) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequestUser.getUserName(), jwtRequestUser.getPassword())
            );
            if(! authentication.isAuthenticated()){
                throw new UsernameNotFoundException("Invalid user or password !!!");
            }
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserdataEntity userdataEntity = userDataRepository.findByUsername(jwtRequestUser.getUserName());
            String token = jwtTokenUtil.generateToken(userdataEntity);
            JwtDtoV2 tokenResponse = new JwtDtoV2(token);
            return ResponseEntity.ok(tokenResponse);
        }catch (BadCredentialsException ex){
            throw new UnauthorizedLoginException("Username or Password is Incorrect");
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Object> validateToken(@RequestHeader("Authorization") String requestTokenHeader) {
        Claims claims = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7); try {
                claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
            }
            catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,
                    "JWT Token does not begin with Bearer String");
        }
        return ResponseEntity.ok(claims);
    }

    @ExceptionHandler(UnauthorizedLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> UnauthorizedLoginException(UnauthorizedLoginException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.UNAUTHORIZED.value(), null,ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
    }

}
