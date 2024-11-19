package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.mail.FormMailDto;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@Service
public class MISLService {

    @Value("${microsoft.dev.endpoint.uri}")
    private  String uri;



    public JsonNode getInfo(String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri+"/me", HttpMethod.GET, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(response.getBody());
        } else {
            throw new ItemNotFoundException("Failed to get user info. Response: " + response.getBody());
        }
    }

        private JsonNode findUserByEmail(String email,String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(uri+"/"+email, HttpMethod.GET, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(response.getBody());
        } else {
            throw new Exception("Failed to get user info. Response: " + response.getBody());
        }
    }

}
