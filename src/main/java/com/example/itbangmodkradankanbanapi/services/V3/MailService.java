package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.mail.FormMailDto;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoard;
import com.example.itbangmodkradankanbanapi.repositories.V3.ShareBoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.userShare.UserDataRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String EMAIL;

    @Value("${value.url.cross.origin}")
    private String URL;

    @Value("value.mail.from")
    private String TEAM;
    @Autowired
    private JavaMailSender mailSender;



    public void sendInvitationEmail(FormMailDto formMailDto) {
        String inviteLink = URL+"/board/"+formMailDto.getBoardId()+"/collab/invitations";
        String subject = formMailDto.getFrom() +" has invited you to collaborate with " +formMailDto.getRole().toString()+" access right on "+ formMailDto.getBoardName()+" board";
        String message = "<p>You have been invited to collaborate on the board. Click the link below to accept or decline the invitation:</p>"
                + "<p><a href=\"" + inviteLink + "\">Accept Invitation</a></p>";
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(TEAM);
            helper.setText(message, true);
            helper.setTo(formMailDto.getRecipientEmail());
            helper.setSubject(subject);
            helper.setFrom(EMAIL);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
