package com.codeup.volunteertracker.services;


import com.codeup.volunteertracker.models.UserPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("EmailService")
public class EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String from;

    public void prepareAndSend(UserPosition userPosition, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(userPosition.getUser().getEmail());
        msg.setSubject(subject);
        msg.setText(body);

        try{
            this.emailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
//            eventually create a log file with the message error to fix this error for a real fix outside of testing purposes. (HOW WOULD YOU DO THAT????)
        }
    }

//    public void prepareAndSend(User user, String subject, String body) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(from);
////        msg.setTo(book.getAdmin().getEmail());
//        msg.setTo(user.getEmail());
//        msg.setSubject(subject);
//        msg.setText(body);
//
//        try{
//            this.emailSender.send(msg);
//        }
//        catch (MailException ex) {
//            // simply log it and go on...
//            System.err.println(ex.getMessage());
////            eventually create a log file with the message error to fix this error for a real fix outside of testing purposes. (HOW WOULD YOU DO THAT????)
//        }
//    }
}
