package com.codeup.volunteertracker.controllers;


import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.models.UserPosition;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import com.codeup.volunteertracker.repositories.UserPositionRepository;
import com.codeup.volunteertracker.repositories.UserRepository;
import com.codeup.volunteertracker.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.*;

@Controller
public class EventController {

//    starting dependency injection.....
    private final EventRepository eventDao;
    private final UserRepository userDao;
    private final PositionRepository positionDao;
    private final UserPositionRepository userPositionDao;

    public EventController(EventRepository eventRepository, UserRepository userRepository, PositionRepository positionDao, UserPositionRepository userPositionRepository){
        this.eventDao= eventRepository;
        this.userDao = userRepository;
        this.positionDao = positionDao;
        this.userPositionDao = userPositionRepository;
    }

    @Autowired
    private EmailService emailService;

    @Value("${mapToken}")
    private String mapToken;

    @Value("${filestack-api-key}")
    private String filestackAPI;

// LIST OF EVENTS
    @GetMapping("/events")
    public String eventIndex(Model viewModel){
        Iterable<Event> events = eventDao.findAll();
        viewModel.addAttribute("events", events);
        Iterable<Position> positions = positionDao.findAll();
        viewModel.addAttribute("positions", positions);
      
        return "events/index";
    }

// SHOW INDIVIDUAL EVENT
    @GetMapping("/events/{id}")
    public String showClickedEvent(@PathVariable long id, Model viewModel){
        Event event = eventDao.findOne(id);
        viewModel.addAttribute("event", event);
        User eventUser = event.getCreator();
        viewModel.addAttribute("eventUser", eventUser);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            String userId = "";
            viewModel.addAttribute("userId", userId);
        } else {
            User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = userDao.findOne(userSession.getId());

            List<Long> userPos = currentUser.getUserPositionIdsByEventId(id);

//            List<UserPosition> userPos = currentUser.getUserPosition();
            viewModel.addAttribute("userPos", userPos);
            long userId = userSession.getId();
            viewModel.addAttribute("userId", userId);
        }

        Iterable<Position> positions = event.getPositions();
        viewModel.addAttribute("positions", positions);

        Iterable<UserPosition> userPositions = userPositionDao.findAll();
        viewModel.addAttribute("userPositions", userPositions);

        viewModel.addAttribute("mapToken", mapToken);



        return "events/show";
    }

//    CREATE EVENT
    @GetMapping("/events/create")
    public String createEvent(Model viewModel){
        viewModel.addAttribute("event", new Event());
        viewModel.addAttribute("mapToken", mapToken);
        viewModel.addAttribute("filestackAPI", filestackAPI);
        return "events/create-event";
    }

    @PostMapping("/events/create")
    public String createEvent(@RequestParam(name="location") String location, @RequestParam(name="address") String address, @RequestParam(name = "start") String start, @RequestParam(name = "stop") String stop, @RequestParam(name = "title") String title, @RequestParam(name="description") String description, @RequestParam(name="file") String photo, @RequestParam(name="organization") String organization) throws ParseException {
            DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");
            Date localTimeObj1 = df.parse(start);
            Date localTimeObj2 = df.parse(stop);
            Event event = new Event();
            event.setId(event.getId());
            event.setStart(localTimeObj1);
            event.setStop(localTimeObj2);
            event.setDescription(description);
            event.setLocation(location);
            event.setAddress(address);
            event.setTitle(title);
            event.setOrganization(organization);
            event.setPhoto(photo);
            User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userDao.findOne(userSession.getId());
            event.setCreator(user);
            Event createEvent = eventDao.save(event);

        emailService.createdEvent(createEvent, "Creation of Volunteer Event", String.format("Let's get the community together to volunteer by giving back and helping you with your good deed.\n\n  Spread the word to gather volunteers needed for this event with the following link: https://pathofthevolunteer.com/events/%d", createEvent.getId()));
            return "redirect:/events/" + createEvent.getId() + "/create-position";
    }

//    EDIT EVENT
    @GetMapping("/events/edit/{id}")
    public String editEvent(@PathVariable long id, Model viewModel){
        viewModel.addAttribute("event", eventDao.findOne(id));
        viewModel.addAttribute("mapToken", mapToken);
        viewModel.addAttribute("filestackAPI", filestackAPI);

        return "events/edit-event";
    }

    //might need to surround parse with try/catch
    @PostMapping("/events/edit/{id}")
    public String editEvent(@PathVariable long id, @RequestParam(name="title") String title, @RequestParam(name="start") String start, @RequestParam(name="stop") String stop, @RequestParam(name="location") String location, @RequestParam(name="address") String address, @RequestParam(name="file") String photo, @RequestParam(name="description") String description, @RequestParam(name="organization") String organization) throws ParseException {
        Event editedEvent = eventDao.findOne(id);
        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");
        Date newStart = df.parse(start);
        Date newStop = df.parse(stop);
        editedEvent.setId(id);
        editedEvent.setTitle(title);
        editedEvent.setStart(newStart);
        editedEvent.setStop(newStop);
        editedEvent.setLocation(location);
        editedEvent.setAddress(address);
        editedEvent.setDescription(description);
        editedEvent.setOrganization(organization);
        editedEvent.setPhoto(photo);
        eventDao.save(editedEvent);
        return "redirect:/events/" + id;
    }

//    DELETE EVENT
    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable long id) {
        Event toDelete = eventDao.findOne(id);
        long eventId = toDelete.getId();
        List<Position> positions = positionDao.findAllByEvent_Id(eventId);
        for ( Position position : positions) {
            long positionId = position.getId();
            List<UserPosition> userPositions = userPositionDao.findAllByPosition_Id(positionId);
            for (UserPosition userPosition : userPositions){
                long userPositionId = userPosition.getId();
                userPositionDao.delete(userPositionId);
            }
            positionDao.delete(positionId);
        }
        eventDao.delete(eventId);

        emailService.createdEvent(toDelete, "Deletion of Volunteer Event", String.format("An event has been removed under your profile.\n\n  If you would like to create any future events please visit our website at https://pathofthevolunteer.com ."));

        return "redirect:/events";
    }

    @PostMapping("/events/delete/{id}")
    public String afterDelete(){
        return "redirect:/events";
    }

//    Approve hours
    @GetMapping("/events/approve/{id}")
    public String showApprovePage(@PathVariable long id, Model model) {
        Event event = eventDao.findOne(id);
        model.addAttribute("event", event);
        User eventUser = event.getCreator();

        model.addAttribute("eventUser", eventUser);

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            String userId = "";
            model.addAttribute("userId", userId);
        } else {
            User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            long userId = userSession.getId();
            model.addAttribute("userId", userId);
        }

        List<Position> positions = positionDao.findAllByEvent_Id(id);
        Map<Position, List> volunteers = new HashMap<>();
        for(Position position : positions) {
            List<UserPosition> userPositions = userPositionDao.findAllByPosition(position);
            volunteers.put(position, userPositions);
        }
        model.addAttribute("volunteers", volunteers);
        return "events/approveHours";
    }

    @PostMapping("/events/approve")
    public String submitHours(@RequestParam(name = "check", defaultValue = "off") long[] isApproved,
                              @RequestParam(name = "eventId") long eventId) throws ParseException {
        Event event = eventDao.findOne(eventId);
        for(long userPos : isApproved) {
            UserPosition userPosition = userPositionDao.findOne(userPos);
            userPosition.setApproved(true);
            userPositionDao.save(userPosition);

            Position position = positionDao.findOne(userPosition.getPosition().getId());
            long shiftHours = event.posHours(position.getStart(),position.getEnd());

            User user = userDao.findOne(userPosition.getUser().getId());
            long currentHours = user.getHours();
            user.setHours(currentHours+shiftHours);
            userDao.save(user);

            DateFormat fmt = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            String dateString = fmt.format(event.getStart());


            emailService.createdAnAccount(user, "Letter of Appreciation", String.format(user.getFirstName() + " " + user.getLastName() + ",\n\n On behalf of the " + event.getOrganization() + ", we would like to thank you for taking the time to volunteer at the " + event.getTitle() + " that took place on " + dateString + ". We could not have had such an amazing event without your help and support.\n\n By participating for a total of " + shiftHours + " hour(s) as a " + position.getTitle() + " volunteer, you aided us in bringing the community closer together and giving back. We hope to see you again as a volunteer at future events.\n\n If you have any questions or need to get into contact with the organizer of this event, please do so at " + event.getCreator().getEmail() + " or " + event.getCreator().getPhoneNumber() + ". \n\n\n Sincerely,\n\n " + event.getCreator().getFirstName() + " " + event.getCreator().getLastName() + "\n" + event.getOrganization()));
        }
        return "redirect:/events/" + eventId;
    }

//    Volunteer contact info
    @GetMapping("/events/volunteers/{id}")
    public String showVolunteersPage(@PathVariable long id, Model model) {
        Event event = eventDao.findOne(id);
        model.addAttribute("event", event);
        List<Position> positions = positionDao.findAllByEvent_Id(id);
        Map<Position, List> volunteers = new HashMap<>();
        for(Position position : positions) {
            List<UserPosition> userPositions = userPositionDao.findAllByPosition(position);
            volunteers.put(position, userPositions);
        }
        model.addAttribute("volunteers", volunteers);
        return "events/volunteerInfo";
    }
}
