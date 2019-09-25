package com.codeup.volunteertracker.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "SID FROM TWILIO ACCOUNT";
    public static final String AUTH_TOKEN = "auth token";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber("+12542319966"),
                new PhoneNumber("+12543122613"),
                "This is the ship that made the Kessel Run in fourteen parsecs?").create();

        System.out.println(message.getSid());
    }
}
