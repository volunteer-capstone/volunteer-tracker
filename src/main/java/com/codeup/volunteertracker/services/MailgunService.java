package com.codeup.volunteertracker.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MailgunService extends Secret {

    @GetMapping("/email")
    public static String email(){

        return "email";
    }

    @PostMapping("/email")
    public static JsonNode sendSimpleMessage() throws UnirestException {
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/mg.pathofthevolunteer.com/messages")
//                .basicAuth("api", PASSWORD)
                .field("from", "Tester for Path of the Volunteer <USER@YOURDOMAIN.COM>")
                .field("to", "brandiclinard021911@gmail.com")
                .field("subject", "Testing Mailgun email service")
                .field("text", "This is an email test for path of the volunteer.")
                .asJson();

        return request.getBody();

    }
}
