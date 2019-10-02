package com.codeup.volunteertracker.services;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.models.UserPosition;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import com.codeup.volunteertracker.repositories.UserPositionRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.twilio.example.Example.ACCOUNT_SID;
import static com.twilio.example.Example.AUTH_TOKEN;

@Controller
public class TwilioService{
    private final PositionRepository positionDao;
    private final UserPositionRepository userPositionDao;
    private final EventRepository eventDao;

    public TwilioService(PositionRepository positionRepository,UserPositionRepository userPositionRepository, EventRepository eventRepository){
        this.positionDao = positionRepository;
        this.userPositionDao = userPositionRepository;
        this.eventDao = eventRepository;
    }
    // Find your Account Sid and Token at twilio.com/user/account

    @Value("${twilio-api-key}")
    private String twilioKey;

    @Value("${twilio-account-sid}")
    private String twilioAccountSid;

    @Value("${twilio-phone-number}")
    private String twilioPhoneNumber;



    @GetMapping("/text/position/{id}")
    public String testingText(@PathVariable long id){
        long eventId = positionDao.positionEventId(id);

        return "redirect:/events/" + eventId;
//        return "text";
    }

    @PostMapping("/text/position/{id}")
    public String testing(@PathVariable long id, @RequestParam(name="body") String body){
//        System.out.println("Success 2");
        Twilio.init(twilioAccountSid, twilioKey);
        Position position = positionDao.findOne(id);
//        System.out.println(position);
        List<UserPosition> userPositions = userPositionDao.findAllByPosition(position);
//        System.out.println(userPositions);
        for(UserPosition userPosition: userPositions){
//            User user = userPosition.getUser();
            String phoneNumber = "+1" + userPosition.getUser().getPhoneNumber();
//            System.out.println(phoneNumber);

            Message message = Message.creator(new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber), body).create();

            System.out.println(message.getSid());
//            System.out.println(phoneNumber);
        }
        long eventId = positionDao.positionEventId(id);

        return "redirect:/events/" + eventId;
    }

//        return "redirect:/events";
//    }
}

