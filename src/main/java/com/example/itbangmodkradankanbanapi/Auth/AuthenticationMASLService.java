package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.entities.userThirdParty.UserThirdParty;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.models.UserThirdPartyPlatform;
import com.example.itbangmodkradankanbanapi.repositories.userThirdParty.UserThirdPartyRepository;
import com.example.itbangmodkradankanbanapi.services.V3.MISLService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationMASLService {

    @Value("${jwt.access.token.cookie.name}")
    private String jwtCookie;
    @Value("${jwt.ref.access.token.cookie.name}")
    private String jwtRefCookie;

    @Value("${microsoft.access.token.cookie.name}")
    private String microsoftAccessToken;

    @Value("${value.microsoft.tenant}")
    private String tenant;

    @Value("${value.server.address}")
    private String serverAddress;

    @Value("${spring.security.oauth2.client.registration.azure.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.azure.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.azure.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.registration.azure.scope}")
    private String scope;


    @Value("${spring.security.oauth2.client.registration.azure.client-secret}")
    private String clientSecret;



    @Autowired
    private MISLService mislService;

    @Value("${spring.security.oauth2.client.provider.azure.token-uri}")
    private String tokenUri;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserThirdPartyRepository userThirdPartyRepository;


    public String login() {
   return UriComponentsBuilder.fromHttpUrl(authorizationUri)
           .queryParam("client_id", clientId)
           .queryParam("response_type", "code")
           .queryParam("redirect_uri", redirectUri)
           .queryParam("scope", "openid profile email User.Read User.ReadBasic.All")
           .queryParam("response_mode", "query")
           .toUriString();
    }

    @Transactional
    public Map<String,String> exchangeAuthorizationCodeForToken(String authorizationCode) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // สร้างข้อมูลใน request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("code", authorizationCode);
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            JsonNode userInfo = mislService.getInfo(jsonResponse.get("access_token").asText());

            UserThirdParty userThirdParty = userThirdPartyRepository.findById(userInfo.get("id").asText()).orElse(null);
            if(userThirdParty == null)
            {
                userThirdParty = new UserThirdParty();
                userThirdParty.setOid(userInfo.get("id").asText());
                userThirdParty.setPlatform(UserThirdPartyPlatform.MICROSOFT);
            }
            userThirdParty.setName(userInfo.get("displayName").asText());
            userThirdParty.setEmail(userInfo.get("userPrincipalName").asText());
            userThirdPartyRepository.save(userThirdParty);

            ResponseCookie accessTokenCookie = jwtTokenUtil.generateCookieThirdParty(userThirdParty);
            ResponseCookie refreshTokenCookie = jwtTokenUtil.generateRefreshCookieThirdParty(userThirdParty);
            ResponseCookie micJwtAccessToken = jwtTokenUtil.generateCookie(microsoftAccessToken,jsonResponse.get("access_token").asText());

            Map<String,String> result = new HashMap<>();

            result.put(jwtCookie,accessTokenCookie.toString());
            result.put(jwtRefCookie,refreshTokenCookie.toString());
            result.put(microsoftAccessToken,micJwtAccessToken.toString());
            return result;
        } else {
            throw new Exception("Failed to exchange authorization code. Response: " + response.getBody());
        }
    }

    public String logout() {
        return UriComponentsBuilder.fromHttpUrl("https://login.microsoftonline.com/"+tenant+"/oauth2/v2.0/logout")
                .queryParam("post_logout_redirect_uri",serverAddress+"/auth/misl/callback/logout" )
                .buildAndExpand()
                .toUriString();
    }

    public Map<String,String> logoutCallback() {
        Map<String,String> result = new HashMap<>();
        result.put(jwtCookie,jwtTokenUtil.removeCookie(jwtCookie).toString());
        result.put(jwtRefCookie,jwtTokenUtil.removeCookie(jwtRefCookie).toString());
        result.put(microsoftAccessToken,jwtTokenUtil.removeCookie(microsoftAccessToken).toString());
        return result;
    }




}
