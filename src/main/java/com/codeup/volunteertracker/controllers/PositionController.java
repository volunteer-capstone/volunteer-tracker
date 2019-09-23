package com.codeup.volunteertracker.controllers;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public class PositionController {

    private final PositionRepository positionDao;
    private final EventRepository eventDao;

    public PositionController(PositionRepository positionRepository, EventRepository eventRepository){
        this.positionDao = positionRepository;
        this.eventDao = eventRepository;
    }

//    UNTESTED-- create
    //will have to pass event id as a hidden attribute
    @GetMapping("/events/{id}/create-position")
    public String createPosition(Model viewModel, @PathVariable long id){
        viewModel.addAttribute("position", new Position());
        Event event = eventDao.findOne(id);
        viewModel.addAttribute("event", event);
        return "/position/create";
    }

    // UNTESTED-- create
    @PostMapping("/events/{id}/create-position")
    public String createPosition(@ModelAttribute Position position, @ModelAttribute Event event){
        position.setEvent(event);
        Position savePosition = positionDao.save(position);
        return "redirect:/events/{id}";
    }

    @GetMapping("/events/positions/edit/{id}")
   public String editPosition(@PathVariable long id, Model viewModel){
        viewModel.addAttribute("position", positionDao.findOne(id));
        return "positions/edit";
    }


//    @PostMapping("/events/positions/edit/{id}")
//    public String editPosition(@PathVariable, long id, @RequestParam(name="title"), String title, @RequestParam(name="description") String description, @RequestParam(name="numNeeded") long numNeeded, @RequestParam(name="start") Datetime start, @RequestParam(name="end") DateTime end) {
//
//    }
}
