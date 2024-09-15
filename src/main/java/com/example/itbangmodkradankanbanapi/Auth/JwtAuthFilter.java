package com.example.itbangmodkradankanbanapi.Auth;


import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedException;
import com.example.itbangmodkradankanbanapi.exceptions.UnauthorizedLoginException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // ตรวจสอบให้แน่ใจว่าการตรวจสอบ Token จะไม่เกิดขึ้นสำหรับ /authentications/login หรือ /authentications/validate-token
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/login") || requestURI.equals("/validate-token")) {
            chain.doFilter(request, response);
            return;
        }
//       2) ตรวจสอบว่า access token ถูกส่งใน header ของ request
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

//       3) กรณีไม่มี token หรือ token ไม่เริ่มต้นด้วย "Bearer ":
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (ExpiredJwtException e) {
                throw new UnauthorizedException("Token is expired");
            } catch (IllegalArgumentException e) {
                throw new UnauthorizedException("Unable to get JWT Token");
            } catch (Exception e) {
                throw new UnauthorizedException("Invalid JWT Token");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////              6) เพื่อตรวจสอบความถูกต้องของ token
//                response.getWriter().write("{\"error\": \"Invalid JWT Token\"}");
//                response.getWriter().flush();
//                return;
            }

        } else if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()); usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            chain.doFilter(request, response);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid JWT Token\"}");
                response.getWriter().flush();
                return;
            }

        }

        chain.doFilter(request, response);

    }
}