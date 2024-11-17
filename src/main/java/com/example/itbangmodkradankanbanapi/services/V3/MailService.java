package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.mail.FormMailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    @Value("${value.mail.invite}")
    private String URL;

    @Value("${value.mail.from}")
    private String TEAM;
    @Autowired
    private JavaMailSender mailSender;



    public Boolean sendInvitationEmail(FormMailDto formMailDto) {
        String roleInvite = formMailDto.getRole().toString().equals("READER") ? "READ" : "WRITE";
        String inviteLink = URL+"/board/"+formMailDto.getBoardId()+"/collab/invitations";
        String subject = formMailDto.getFrom() +" has invited you to collaborate with " + roleInvite +" access right on "+ formMailDto.getBoardName()+" board";
        String message = "<p>You have been invited to collaborate on the board. Click the link below to accept or decline the invitation:</p>"
                + "<p><a href=\"" + inviteLink + "\">Accept Invitation</a></p>";
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message, true);
            helper.setTo(formMailDto.getRecipientEmail());
            helper.setSubject(subject);
            helper.setFrom("noreply@intproj23.sit.kmutt.ac.th",TEAM);
            mailSender.send(mimeMessage);

            return true;
        } catch (MessagingException e) {
            return false;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
