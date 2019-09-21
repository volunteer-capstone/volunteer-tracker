package com.codeup.volunteertracker.controllers;


import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/events")
    public String eventIndex(Model viewModel){
//        code to iterate event list will go here
        Iterable<Event> events = eventDao.findAll();
        viewModel.addAttribute("events", events);
        return "events/index";
    }


    @GetMapping("/events/{id}")
    public String showClickedEvent(@PathVariable long id, Model viewModel){
//        code to show individual event will go here
        Event event = eventDao.findOne(id);
        viewModel.addAttribute("event", event);
        return "events/show";
    }
}
