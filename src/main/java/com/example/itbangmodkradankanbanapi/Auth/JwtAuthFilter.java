package com.example.itbangmodkradankanbanapi.Auth;


import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoard;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import com.example.itbangmodkradankanbanapi.entities.user.UserdataEntity;
import com.example.itbangmodkradankanbanapi.entities.userThirdParty.UserThirdParty;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.repositories.V3.BoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.ShareBoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.user.UserDataCenterRepository;
import com.example.itbangmodkradankanbanapi.repositories.userThirdParty.UserThirdPartyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private ShareBoardRepositoryV3 shareBoardRepository;
    @Autowired
    private BoardRepositoryV3 boardRepository;

    @Autowired
    UserThirdPartyRepository userThirdPartyRepository;


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

        if (requestURI.equals("/login") || requestURI.matches("/auth/misl/[^/]+") ||   requestURI.matches("/swagger-ui/[^/]+") || requestURI.equals("/v3/api-docs") || requestURI.matches("/v3/api-docs/[^/]+")   ||  requestURI.matches("/v1/[^/]+") || requestURI.matches("/v2/[^/]+")  || requestURI.equals("/validate-token") || requestURI.equals("/v2/colors") || requestURI.equals("/token") || requestURI.equals("/clear-cookie")) {
            chain.doFilter(request, response);
            return;
        }



        String[] uriParts = requestURI.split("/");
        String nanoId = "";
        Board board = null;
        String jwtToken = jwtTokenUtil.getTokenCookie(request.getCookies());
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

            }else if(request.getMethod().equals("GET") && jwtToken == null){
                ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.FORBIDDEN.value(), null, "no access for this action", request.getRequestURI());
                writeErrorResponse(response, er);
                return;
            }
        }

        String username = null;
        if (jwtToken == null) {
            ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.UNAUTHORIZED.value(), null, "JWT Token is null", request.getRequestURI());
            writeErrorResponse(response, er);
            return;
        }
        String oid = jwtTokenUtil.getAllClaimsFromToken(jwtToken).get("oid").toString();
        Claims claims ;
        try {
            claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
            jwtTokenUtil.validateToken(jwtToken);
            username = claims.getSubject();
        if(claims.containsKey("platform"))
        {
            oid = claims.get("oid").toString();
           UserThirdParty userThirdParty = userThirdPartyRepository.findById(oid).orElse(null);
            if(userThirdParty.equals(null)) {
                ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.NOT_FOUND.value(), null, "User id " + oid + " not found", request.getRequestURI());
                writeErrorResponse(response, er);
                return;
            }
            Authentication thirdPartyAuth = new AbstractAuthenticationToken(null) {
                @Override
                public boolean isAuthenticated() {
                    return true;
                }

                @Override
                public Object getPrincipal() {
                    return userThirdParty;
                }

                @Override
                public Object getCredentials() {
                    return null;
                }
            };

            SecurityContextHolder.getContext().setAuthentication(thirdPartyAuth);


        }else {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

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



        if (requestURI.equals("/v3/boards") || requestURI.equals("/v3/collabs/receive-invite") || requestURI.equals("/auth/misl/callback") || (requestURI.matches("/v3/boards/[^/]+/invite/[^/]+") && request.getMethod().equals("GET"))   || requestURI.matches("/user-info") || requestURI.equals("/v3/collabs")  ) {
            chain.doFilter(request, response);
            return;
        }


        ShareBoard shareBoard = shareBoardRepository.findByOidUserShareAndBoard(oid, board);
        if(shareBoard != null) {
            if (request.getMethod().equals("GET") || (requestURI.matches("/v3/boards/[^/]+/collabs/[^/]+") && request.getMethod().equals("DELETE"))) {
                chain.doFilter(request, response);
                return;
            }

            if (shareBoard.getRole().equals(ShareBoardsRole.WRITER) && !requestURI.matches("boards/[^/]+/invite/[^/]+") && !(requestURI.matches("/v3/boards/[^/]+/collabs") && request.getMethod().equals("POST")) && !request.getMethod().equals("PATCH")) {
                chain.doFilter(request, response);
                return;
            }

            if (shareBoard.getRole().equals(ShareBoardsRole.OWNER)) {
                chain.doFilter(request, response);
                return;
            }
        }


        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.FORBIDDEN.value(), null, "No access for this action", request.getRequestURI());
        writeErrorResponse(response, er);

    }

}