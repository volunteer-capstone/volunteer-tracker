package com.codeup.volunteertracker.controllers;


import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.models.UserPosition;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import com.codeup.volunteertracker.repositories.UserPositionRepository;
import com.codeup.volunteertracker.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

//NONE TESTED YET

    @GetMapping("/events")
    public String eventIndex(Model viewModel){
        Iterable<Event> events = eventDao.findAll();
        viewModel.addAttribute("events", events);
        return "events/index";
    }


    @GetMapping("/events/{id}")
    public String showClickedEvent(@PathVariable long id, Model viewModel){
        Event event = eventDao.findOne(id);
        viewModel.addAttribute("event", event);
        System.out.println(event.getId());
        System.out.println(event.getCreator().getUsername());
        long userId = event.getCreator().getId();
        System.out.println(userId);
        User user = userDao.findOne(userId);
        viewModel.addAttribute("user", user);
        return "events/show";
    }

//    CREATE EVENT
    @GetMapping("/events/create")
    public String createEvent(Model viewModel){
        viewModel.addAttribute("event", new Event());
        return "events/create-event";
    }

    @PostMapping("/events/create")
    public String createEvent(@RequestParam(name="location") String location, @RequestParam(name = "start") String start, @RequestParam(name = "stop") String stop, @RequestParam(name = "title") String title, @RequestParam(name="description") String description) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date localTimeObj1= df.parse(start);
        Date localTimeObj2 = df.parse(stop);
        Event event = new Event();
        event.setId(event.getId());
        event.setStart(localTimeObj1);
        event.setStop(localTimeObj2);
        event.setDescription(description);
        event.setLocation(location);
        event.setTitle(title);
        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.findOne(userSession.getId());
        event.setCreator(user);
        Event createEvent = eventDao.save(event);
        System.out.println(createEvent.getStart());
        System.out.println(createEvent.getStart());
        System.out.println(createEvent.getStop());
        System.out.println(createEvent.getDescription());
        System.out.println(createEvent.getTitle());
        System.out.println(createEvent.getLocation());
        System.out.println(createEvent.getId());
        return "redirect:/events/" + createEvent.getId() + "/create-position";
    }

//    EDIT EVENT
    @GetMapping("/events/edit/{id}")
    public String editEvent(@PathVariable long id, Model viewModel){
        viewModel.addAttribute("event", eventDao.findOne(id));
        return "events/edit-event";
    }

    //might need to surround parse with try/catch
    @PostMapping("/events/edit/{id}")
    public String editEvent(@PathVariable long id, @RequestParam(name="title") String title, @RequestParam(name="start") String start, @RequestParam(name="stop") String stop, @RequestParam(name="location") String location, @RequestParam(name="description") String description) throws ParseException {
        Event editedEvent = eventDao.findOne(id);
        System.out.println(title);
        System.out.println(start);
        System.out.println(stop);
        System.out.println(location);
        System.out.println(description);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date newStart = df.parse(start);
        Date newStop = df.parse(stop);
        editedEvent.setId(id);
        editedEvent.setTitle(title);
        editedEvent.setStart(newStart);
        editedEvent.setStop(newStop);
        editedEvent.setLocation(location);
        editedEvent.setDescription(description);
        Event saveEvent = eventDao.save(editedEvent);
        return "redirect:/events/" + id;
    }

//    DELETE EVENT
    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable long id) {
        Event toDelete = eventDao.findOne(id);
        long eventId = toDelete.getId();
        List<Position> positions = positionDao.findByEvent_Id(eventId);
        System.out.println(positions);
        for ( Position position : positions) {
            long positionId = position.getId();
            List<UserPosition> userPositions = userPositionDao.findByPosition_Id(positionId);
            for (UserPosition userPosition : userPositions){
                long userPositionId = userPosition.getId();
                userPositionDao.delete(userPositionId);
            }
            positionDao.delete(positionId);
        }
        eventDao.delete(eventId);
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
        List<Position> positions = event.getPositions();

        model.addAttribute("event", eventDao.findOne(id));
        model.addAttribute("positions", positions);
        return "events/approveHours";
    }

}
