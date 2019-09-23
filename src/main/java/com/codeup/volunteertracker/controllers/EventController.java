package com.codeup.volunteertracker.controllers;


import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class EventController {

//    starting dependency injection.....
    private final EventRepository eventDao;
    private final UserRepository userDao;

    public EventController(EventRepository eventRepository, UserRepository userRepository){
        this.eventDao= eventRepository;
        this.userDao = userRepository;
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
        return "events/create";
    }

    @PostMapping("/events/create")
    public String createEvent(@ModelAttribute Event event){
        Event createEvent = eventDao.save(event);
        return "events/create";
    }

//    EDIT EVENT
    @GetMapping("/events/edit/{id}")
    public String editEvent(@PathVariable long id, Model viewModel){
        viewModel.addAttribute("event", eventDao.findOne(id));
        return "events/edit";
    }

    @PostMapping("/events/edit/{id}")
    public String editEvent(@PathVariable long id, @RequestParam(name="title") String title, @RequestParam(name="start") Date start, @RequestParam(name="stop") Date stop, @RequestParam(name="location") String location, @RequestParam(name="description") String description){
        Event editedEvent = eventDao.findOne(id);
        editedEvent.setTitle(title);
        editedEvent.setStart(start);
        editedEvent.setStop(stop);
        editedEvent.setLocation(location);
        editedEvent.setDescription(description);
        return "redirect:/events/" + id;
    }

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
}
