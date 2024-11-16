package com.example.itbangmodkradankanbanapi.Auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthenticationMASLService {

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


    @Value("${spring.security.oauth2.client.provider.azure.token-uri}")
    private String tokenUri;

    public String login() {
   return UriComponentsBuilder.fromHttpUrl(authorizationUri)
           .queryParam("client_id", clientId)
           .queryParam("response_type", "code")
           .queryParam("redirect_uri", redirectUri)
           .queryParam("scope", scope)
           .queryParam("response_mode", "query")
           .toUriString();
    }

    public String exchangeAuthorizationCodeForToken(String authorizationCode) throws Exception {
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
            // ดึง Access Token จาก Response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            System.out.println(jsonResponse.toString());

            return jsonResponse.get("access_token").asText();
        } else {
            throw new Exception("Failed to exchange authorization code. Response: " + response.getBody());
        }
    }
}
