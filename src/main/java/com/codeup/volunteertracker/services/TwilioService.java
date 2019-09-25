package com.codeup.volunteertracker.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TwilioService {
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "AC4c785c0ddfbe4f35486902bcf9ed9a88";
    public static final String AUTH_TOKEN = "ebc9ba8e0953e821460daf2f324b98b6";



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
