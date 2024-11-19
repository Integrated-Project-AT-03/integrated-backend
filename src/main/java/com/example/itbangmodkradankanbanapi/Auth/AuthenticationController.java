package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.entities.user.UserdataEntity;
import com.example.itbangmodkradankanbanapi.entities.userThirdParty.UserThirdParty;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedLoginException;
import com.example.itbangmodkradankanbanapi.models.TokenResponse;
import com.example.itbangmodkradankanbanapi.repositories.user.UserDataCenterRepository;
import com.example.itbangmodkradankanbanapi.repositories.userThirdParty.UserThirdPartyRepository;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;


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
    UserDataCenterRepository userDataCenterRepository;

    @Autowired
    UserThirdPartyRepository userThirdPartyRepository;

    private String jwtCookie;
    @Value("${jwt.ref.access.token.cookie.name}")
    private String jwtRefCookie;

    @Operation(summary = "User Login", description = "Authenticates the user and returns JWT and Refresh cookies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request - validation failed")
    })
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid JwtRequestUser jwtRequestUser) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequestUser.getUserName(), jwtRequestUser.getPassword())
            );
            if (!authentication.isAuthenticated()) {
                throw new UsernameNotFoundException("Invalid user or password !!!");
            }
            UserdataEntity userdataEntity = userDataCenterRepository.findByUsername(jwtRequestUser.getUserName());
            ResponseCookie jwtCookie = jwtTokenUtil.generateJwtCookie(userdataEntity);
            ResponseCookie refJwtCookie = jwtTokenUtil.generateRefreshJwtCookie(userdataEntity);

            Map<String, Object> claims = jwtTokenUtil.getAllClaimsFromToken(jwtCookie.getValue());
            TokenResponse response = new TokenResponse(claims, "hidden", "hidden");

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refJwtCookie.toString())
                    .body(response);
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedLoginException("Username or Password is Incorrect");
        }
    }







    @Operation(summary = "Refresh Token", description = "Generates new JWT and Refresh tokens using an existing Refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or expired refresh token")
    })
    @PostMapping("/token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        Map<String,String> cookieMap = jwtTokenUtil.getMapCookie(request.getCookies());
        String jwtRefToken = cookieMap.getOrDefault(jwtRefCookie,null) ;
        if(jwtRefToken == null ) throw new UnauthorizedLoginException("Not found refreshToken");
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtRefToken);
        String oid = claims.get("oid").toString();
        ResponseCookie jwtCookie;
        ResponseCookie refJwtCookie;
        if(claims.containsKey("platform"))
        {
           UserThirdParty userThirdParty = userThirdPartyRepository.findById(oid).orElseThrow(() -> new ItemNotFoundException("The user has not register in app yet"));
            jwtCookie = jwtTokenUtil.generateCookieThirdParty(userThirdParty);
            refJwtCookie = jwtTokenUtil.generateRefreshCookieThirdParty(userThirdParty);
        }else {
            UserdataEntity userdataEntity = userDataCenterRepository.findById(oid).orElse(null);
            jwtCookie = jwtTokenUtil.generateJwtCookie(userdataEntity);
            refJwtCookie = jwtTokenUtil.generateRefreshJwtCookie(userdataEntity);
        }

        TokenResponse response = new TokenResponse(claims, "hidden", "hidden");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refJwtCookie.toString())
                .body(response);
    }

    @Operation(summary = "Get User Information", description = "Retrieves user information from the JWT token in the cookies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user information",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT token")
    })
    @GetMapping("/user-info")
    public ResponseEntity<Object> getInfo(HttpServletRequest request) {
        String jwtToken = jwtTokenUtil.getTokenCookie(request.getCookies());
        return ResponseEntity.ok(jwtTokenUtil.getAllClaimsFromToken(jwtToken));
    }

    @Operation(summary = "Validate Token", description = "Validates the JWT token provided in the cookies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or expired JWT token")
    })
    @GetMapping("/validate-token")
    public ResponseEntity<Object> validateToken(HttpServletRequest request) {
        String jwtToken = jwtTokenUtil.getTokenCookie(request.getCookies());
        return ResponseEntity.ok(jwtTokenUtil.validateToken(jwtToken));
    }

    @Operation(summary = "Logout", description = "Logs out the user by clearing JWT and Refresh cookies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out and cookies cleared",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/clear-cookie")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        ResponseCookie jwtCookie = jwtTokenUtil.removeCookie("jwtToken");
        ResponseCookie jwtRefCookie = jwtTokenUtil.removeCookie("jwtRefToken");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefCookie.toString())
                .body("Logged out and cookie cleared");
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
