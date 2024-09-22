package com.example.itbangmodkradankanbanapi.Auth;


import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;


    private String cookieTokenName;

    private void writeErrorResponse(HttpServletResponse response, ErrorResponse er) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        String json = objectMapper.writeValueAsString(er);
        response.getWriter().write(json);
        response.getWriter().flush();
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/login") || requestURI.equals("/token") ||  requestURI.equals("/validate-token") || requestURI.equals("/v2/colors")) {
            chain.doFilter(request, response);
            return;
        }

        String username = null;
        String jwtToken = jwtTokenUtil.getTokenCookie(request.getCookies());
        System.out.println(jwtToken);


        if (jwtToken == null) {
            ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.UNAUTHORIZED.value(), null, "JWT Token must have", request.getRequestURI());
            writeErrorResponse(response, er);
            return;
        }

            try {
                Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
                username = claims.getSubject();
            } catch (ExpiredJwtException e) {
                ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.UNAUTHORIZED.value(), null, "Token is expired", request.getRequestURI());
                writeErrorResponse(response, er);
                return;
            } catch (IllegalArgumentException e) {
                ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.UNAUTHORIZED.value(), null, "Unable to get JWT Token", request.getRequestURI());
                writeErrorResponse(response, er);
                return;
            } catch (Exception e) {
                ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.UNAUTHORIZED.value(), null, "Invalid JWT Token", request.getRequestURI());
                writeErrorResponse(response, er);
                return;
            }


            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()); usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            chain.doFilter(request, response);
            }
        }


}