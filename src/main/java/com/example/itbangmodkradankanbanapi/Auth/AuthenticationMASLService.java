package com.example.itbangmodkradankanbanapi.Auth;

import com.example.itbangmodkradankanbanapi.entities.userThirdParty.UserThirdParty;
import com.example.itbangmodkradankanbanapi.models.UserThirdPartyPlatform;
import com.example.itbangmodkradankanbanapi.repositories.userThirdParty.UserThirdPartyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthenticationMASLService {

    @Value("${jwt.access.token.cookie.name}")
    private String jwtCookie;
    @Value("${jwt.ref.access.token.cookie.name}")
    private String jwtRefCookie;

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

    @Value("${spring.security.oauth2.client.provider.azure.user-info-uri}")
    private  String infoUri;

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
           .queryParam("scope", "offline_access openid profile email User.Read")
           .queryParam("response_mode", "query")
           .toUriString();
    }

    public ResponseEntity<Object> exchangeAuthorizationCodeForToken(String authorizationCode) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // สร้างข้อมูลใน request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("code", authorizationCode);
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("redirect_uri", redirectUri);

        // สร้าง HTTP Request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        // ส่งคำขอไปยัง Token Endpoint
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);



        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            UserThirdParty userThirdParty = getInfo(jsonResponse.get("access_token").asText());

            ResponseCookie accessTokenCookie = jwtTokenUtil.generateCookieThirdParty(userThirdParty);
            System.out.println(accessTokenCookie.toString());
            ResponseCookie refreshTokenCookie = jwtTokenUtil.generateRefreshCookieThirdParty(userThirdParty);


            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
//                    .body(userThirdParty);
                     .body("Tokens generated successfully");
        } else {
            throw new Exception("Failed to exchange authorization code. Response: " + response.getBody());
        }
    }


    private UserThirdParty getInfo(String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(infoUri, HttpMethod.GET, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            UserThirdParty userThirdParty = userThirdPartyRepository.findById(jsonNode.get("id").asText()).orElse(null);
            if(userThirdParty.equals(null))
            {
                userThirdParty = new UserThirdParty();
                userThirdParty.setOid(jsonNode.get("id").asText());
                userThirdParty.setPlatform(UserThirdPartyPlatform.MICROSOFT);
            }
                userThirdParty.setName(jsonNode.get("displayName").asText());
                userThirdParty.setEmail(jsonNode.get("userPrincipalName").asText());
                userThirdPartyRepository.save(userThirdParty);

            return  userThirdParty;
        } else {
            throw new Exception("Failed to get user info. Response: " + response.getBody());
        }
    }

}
