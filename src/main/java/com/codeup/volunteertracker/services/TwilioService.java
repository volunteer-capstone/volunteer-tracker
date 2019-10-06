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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    public String positionText(@PathVariable long id){
        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Event event = eventDao.findOne(positionDao.positionEventId(id));
        if(userSession !=null && userSession.isOrganizer() && (userSession.getId() == event.getCreator().getId())) {
            long eventId = positionDao.positionEventId(id);
            return "redirect:/events/" + eventId;
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping("/text/position/{id}")
    public String positionTexting(@PathVariable long id, @RequestParam(name="body") String body){
        Twilio.init(twilioAccountSid, twilioKey);
        Position position = positionDao.findOne(id);
        List<UserPosition> userPositions = userPositionDao.findAllByPosition(position);
        for(UserPosition userPosition: userPositions){
            String userNumber = userPosition.getUser().getPhoneNumber();
            userNumber = userNumber.replace("-","");
            userNumber = userNumber.replace("(","");
            userNumber = userNumber.replace(")","");
            String phoneNumber = "+1" + userNumber;
            Message message = Message.creator(new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber), body).create();
            System.out.println(message.getSid());
        }
        long eventId = positionDao.positionEventId(id);
        return "redirect:/events/" + eventId;
    }

    @GetMapping("/text/event/{id}")
    public String eventText(@PathVariable long id){
        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Event event = eventDao.findOne(id);
        if(userSession !=null && userSession.isOrganizer() && (userSession.getId() == event.getCreator().getId())) {
            return "redirect:/users/" + userSession.getId() + "/profile";
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping("/text/event/{id}")
    public String eventTexting(@PathVariable long id, @RequestParam(name="body") String body){
        Twilio.init(twilioAccountSid, twilioKey);
//        Event event = eventDao.findOne(id);
        List <Position> positions = positionDao.findAllByEvent_Id(id);
        for(Position position: positions){
        List<UserPosition> userPositions = userPositionDao.findAllByPosition(position);
            for(UserPosition userPosition: userPositions){
                String userNumber = userPosition.getUser().getPhoneNumber();
                userNumber = userNumber.replace("-","");
                userNumber = userNumber.replace("(","");
                userNumber = userNumber.replace(")","");
                String phoneNumber = "+1" + userNumber;
                Message message = Message.creator(new PhoneNumber(phoneNumber),
                        new PhoneNumber(twilioPhoneNumber), body).create();
                System.out.println(message.getSid());
            }
        }
        return "redirect:/events/" + id;
    }


}

