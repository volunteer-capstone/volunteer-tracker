package com.codeup.volunteertracker.controllers;


import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import com.codeup.volunteertracker.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
public class EventController {

//    starting dependency injection.....
    private final EventRepository eventDao;
    private final UserRepository userDao;
    private final PositionRepository positionDao;

    public EventController(EventRepository eventRepository, UserRepository userRepository, PositionRepository positionDao){
        this.eventDao= eventRepository;
        this.userDao = userRepository;
        this.positionDao = positionDao;
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
        long userId = eventDao.eventCreatorId(id);
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
//        DateTimeFormat formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
//        Date localTimeObj1 = Date.parse(start, formatter);
//        LocalDateTime localTimeObj2 = LocalDateTime.parse(stop, formatter);
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
        event.setCreator(userSession);
        Event createEvent = eventDao.save(event);
        System.out.println(createEvent.getStart());
        System.out.println(createEvent.getStart());
        System.out.println(createEvent.getStop());
        System.out.println(createEvent.getDescription());
        System.out.println(createEvent.getTitle());
        System.out.println(createEvent.getLocation());
        System.out.println(description);
        return "redirect:/events/{id}/create-position";
    }

//    EDIT EVENT
    @GetMapping("/events/edit/{id}")
    public String editEvent(@PathVariable long id, Model viewModel){
        viewModel.addAttribute("event", eventDao.findOne(id));
        return "events/edit";
    }

//    @PostMapping("/events/edit/{id}")
//    public String editEvent(@PathVariable long id, @RequestParam(name="title") String title, @RequestParam(name="start") Date start, @RequestParam(name="stop") Date stop, @RequestParam(name="location") String location, @RequestParam(name="description") String description){
//        Event editedEvent = eventDao.findOne(id);
//        editedEvent.setTitle(title);
//        editedEvent.setStart(start);
//        editedEvent.setStop(stop);
//        editedEvent.setLocation(location);
//        editedEvent.setDescription(description);
//        return "redirect:/events/" + id;
//    }

//    DELETE EVENT
    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable long id) {
        Event toDelete = eventDao.findOne(id);
        long eventId = toDelete.getId();
        eventDao.delete(id);
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
