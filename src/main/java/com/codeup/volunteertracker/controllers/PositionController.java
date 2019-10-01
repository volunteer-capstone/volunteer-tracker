package com.codeup.volunteertracker.controllers;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.models.UserPosition;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import com.codeup.volunteertracker.repositories.UserPositionRepository;
import com.codeup.volunteertracker.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class PositionController {

    private final PositionRepository positionDao;
    private final EventRepository eventDao;
    private final UserPositionRepository userPositionDao;

    public PositionController(PositionRepository positionRepository, EventRepository eventRepository, UserPositionRepository userPositionRepository){
        this.positionDao = positionRepository;
        this.eventDao = eventRepository;
        this.userPositionDao = userPositionRepository;
    }

    @Autowired
    private EmailService emailService;

    @GetMapping("events/{id}/create-position")
    public String createPosition(Model viewModel, @PathVariable long id){
        viewModel.addAttribute("position", new Position());
        Event event = eventDao.findOne(id);
        viewModel.addAttribute("event", event);
        return "events/create-position";
    }

    //  create(go back and wrap create event and post with try catch for the date parse)
    @PostMapping("events/{id}/create-position")
    public String createPosition(
            @PathVariable long id, @RequestParam(name="description") String description, @RequestParam(name="start") String start, @RequestParam(name="end") String end, @RequestParam(name="numNeeded") int numNeeded, @RequestParam(name="title") String title) throws ParseException {
            DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");
            Date starttime = df.parse(start);
            Date endtime = df.parse(end);
            Position position = new Position();
            position.setDescription(description);
            position.setStart(starttime);
            position.setEnd(endtime);
            position.setNumNeeded(numNeeded);
            position.setTitle(title);
            Event event = eventDao.findOne(id);
            position.setEvent(event);
            Position savePosition = positionDao.save(position);

        emailService.createdPosition(savePosition, "Congrats on Creating Your Volunteer Position", String.format("Let's get the community together to volunteer by giving back and helping you with your good deed.  \n\n Spread the word to gather volunteers for this position with the following link: https://pathofthevolunteer.com/events/%d", savePosition.getEvent().getId()));
            return "redirect:/events/" + savePosition.getEvent().getId();
    }

    // EDIT POSITION
    @GetMapping("/events/positions/edit/{id}")
   public String editPosition(@PathVariable long id, Model viewModel){
        Position position = positionDao.findOne(id);
        viewModel.addAttribute("position", position);
        long eventId = positionDao.positionEventId(position.getId());
        Event event = eventDao.findOne(eventId);
        viewModel.addAttribute("event", event);
        return "events/edit-position";
   }



//    NEED TO SURROUND DF WITH TRY CATCH
    @PostMapping("/events/positions/edit/{id}")
    public String editPosition(@PathVariable long id, @RequestParam(name="title") String title, @RequestParam(name="description") String description, @RequestParam(name="numNeeded") int numNeeded, @RequestParam(name="start") String start, @RequestParam(name="end") String end, Model viewModel) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date starttime= df.parse(start);
        Date endtime = df.parse(end);
        Position editedPosition = positionDao.findOne(id);
        editedPosition.setTitle(title);
        editedPosition.setDescription(description);
        editedPosition.setNumNeeded(numNeeded);
        editedPosition.setStart(starttime);
        editedPosition.setEnd(endtime);
        positionDao.save(editedPosition);
        long eventId = positionDao.positionEventId(id);
        return "redirect:/events/" + eventId;
    }

// Delete
    @GetMapping("/events/positions/delete/{id}")
    public String deletePosition(@PathVariable long id){
        Position toDelete = positionDao.findOne(id);
        List<UserPosition> userPositions = userPositionDao.findAllByPosition_Id(toDelete.getId());
        for (UserPosition userPosition : userPositions){
            long userPositionId = userPosition.getId();
            userPositionDao.delete(userPositionId);
        }
        long eventId = toDelete.getEvent().getId();
        positionDao.delete(toDelete);
        return "redirect:/events/" + eventId;
    }

    @PostMapping("/events/positions/delete/{id}")
    public String afterDelete(@PathVariable long id){
        Position toDelete = positionDao.findOne(id);
        long eventId =toDelete.getEvent().getId();
        positionDao.delete(id);

        emailService.createdPosition(toDelete, "Notification: Deletion of Volunteer Position", String.format("One of the positions on the %s event has been removed. \n\n Please visit the following link: https://pathofthevolunteer.com/events/%d to add more positions, if needed.", toDelete.getEvent(), toDelete.getEvent().getId()));
        return "redirect:/events/" + eventId ;
    }

    //volunteer signup
    @GetMapping("/events/positions/{id}/volunteer")
    public String volunteer(@PathVariable long id){
        UserPosition userPosition = new UserPosition();
        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Position position = positionDao.findOne(id);
        userPosition.setUser(userSession);
        userPosition.setPosition(position);
        userPositionDao.save(userPosition);

        long eventId = positionDao.positionEventId(position.getId());
        Event event = eventDao.findOne(eventId);

        emailService.createdUserPosition(userPosition, "Congrats on Volunteering!", String.format("Thank you for volunteering! \n\n This will event take place  at " + event.getLocation() + ", " + event.getAddress() + ".\n\n  Your volunteer slot is from " + position.getStart() + " to " + position.getEnd() + ".\n\n  Please try to arrive 15 minutes early to check in with the organizer.  If you have anymore follow up questions about your event, please reference the following link: https://pathofthevolunteer.com/events/" + eventId + " or contact the organizer, " + event.getCreator().getFirstName() + " " + event.getCreator().getLastName() + " at " +  event.getCreator().getEmail() + " or " +  event.getCreator().getPhoneNumber() + "."));

        return "redirect:/users/" + userSession.getId() + "/profile";
    }

    @PostMapping("/events/positions/{id}/volunteer")
    public String volunteerSignup(@PathVariable long id){
        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return "redirect:/users/" + userSession.getId() + "/profile";
    }


}
