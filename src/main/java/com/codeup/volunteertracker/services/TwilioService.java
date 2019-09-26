package com.codeup.volunteertracker.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static com.twilio.example.Example.ACCOUNT_SID;
import static com.twilio.example.Example.AUTH_TOKEN;

@Controller
public class TwilioService extends Secret {
    // Find your Account Sid and Token at twilio.com/user/account




    @GetMapping("/text")
    public String testingText(){
        return "text";
    }

    @PostMapping("/text")
    public String testing(){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber("+12542319966"),
                new PhoneNumber("+12543122613"),
                "This message is to test path of the volunteer twilio service.").create();

        System.out.println(message.getSid());

        return "redirect:/events";
    }
}
