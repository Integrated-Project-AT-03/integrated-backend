package com.example.itbangmodkradankanbanapi.Auth;


import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoard;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.repositories.V3.BoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.ShareBoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.userShare.UserDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private ShareBoardRepositoryV3 shareBoardRepository;
    @Autowired
    private BoardRepositoryV3 boardRepository;


    private void writeErrorResponse(HttpServletResponse response, ErrorResponse er) throws IOException {
        response.setStatus(er.getStatus());
        response.setContentType("application/json");
        String json = objectMapper.writeValueAsString(er);
        response.getWriter().write(json);
        response.getWriter().flush();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/login") || requestURI.equals("/validate-token") || requestURI.equals("/v2/colors") || requestURI.equals("/token")) {
            chain.doFilter(request, response);
            return;
        }



        String[] uriParts = requestURI.split("/");
        String nanoId = "";
        Board board = null;
        if(requestURI.contains("/v3/boards/")) {
             nanoId = uriParts[3];
             board = boardRepository.findById(nanoId).orElse(null);
             if(board == null){
                 ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.NOT_FOUND.value(), null, "Board id " + nanoId + " not found", request.getRequestURI());
                 writeErrorResponse(response, er);
                 return;
             }
            if (request.getMethod().equals("GET") && board.getIsPublic()) {
                chain.doFilter(request, response);
                return;

            }
        }





        String username = null;
        String jwtToken = jwtTokenUtil.getTokenCookie(request.getCookies());
        if (jwtToken == null) {
            ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.UNAUTHORIZED.value(), null, "JWT Token must have", request.getRequestURI());
            writeErrorResponse(response, er);
            return;
        }
        try {
            jwtTokenUtil.validateToken(jwtToken);
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
            ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.UNAUTHORIZED.value(), null, "no token OR token is invalid", request.getRequestURI());
            writeErrorResponse(response, er);
            return;
        }
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


        if (requestURI.matches("/v3/user/[^/]+/boards") || requestURI.equals("/v3/boards") || requestURI.matches("/user-info")) {
            chain.doFilter(request, response);
            return;
        }


        UserdataEntity userdata = userDataRepository.findByUsername(username);
        ShareBoard shareBoard = shareBoardRepository.findByOidUserShareAndBoard(userdata.getOid(), board);


        if (shareBoard != null && shareBoard.getRole().equals(ShareBoardsRole.OWNER)) {
            chain.doFilter(request, response);
            return;
        }


        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.FORBIDDEN.value(), null, "no access for this action", request.getRequestURI());
        writeErrorResponse(response, er);

    }

}