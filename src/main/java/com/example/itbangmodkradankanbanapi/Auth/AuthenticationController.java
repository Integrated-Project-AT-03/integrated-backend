package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.Auth.JwtRequestUser;
import com.example.itbangmodkradankanbanapi.Auth.JwtTokenUtil;
import com.example.itbangmodkradankanbanapi.Auth.JwtUserDetailsService;
import com.example.itbangmodkradankanbanapi.dtos.V2.JwtDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V3.user.InfoUserDto;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedLoginException;
import com.example.itbangmodkradankanbanapi.repositories.userShare.UserDataRepository;
import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
@RequestMapping("")
public class AuthenticationController {
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ModelMapper mapper;

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
            UserdataEntity userdataEntity = userDataRepository.findByUsername(jwtRequestUser.getUserName());
            ResponseCookie jwtCookie = jwtTokenUtil.generateJwtCookie(userdataEntity);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(jwtTokenUtil.getAllClaimsFromToken(jwtCookie.getValue()));
        }catch (BadCredentialsException ex){
            throw new UnauthorizedLoginException("Username or Password is Incorrect");
        }
    }



    @GetMapping("/user-info")
    public ResponseEntity<Object> getInfo(HttpServletRequest request) {
        String jwtToken = jwtTokenUtil.getTokenCookie(request.getCookies());
        return ResponseEntity.ok(jwtTokenUtil.getAllClaimsFromToken(jwtToken));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Object> validateToken(HttpServletRequest request) {
        String jwtToken = jwtTokenUtil.getTokenCookie(request.getCookies());
        if(jwtToken == null) return ResponseEntity.ok(false);
        String username = jwtTokenUtil.getAllClaimsFromToken(jwtToken).getSubject();
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);


        return ResponseEntity.ok(jwtTokenUtil.validateToken(jwtToken,userDetails));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. Authentication",  request.getDescription(false));
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }




    @ExceptionHandler(UnauthorizedLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> UnauthorizedLoginException(UnauthorizedLoginException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.UNAUTHORIZED.value(), null,ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
    }

}
