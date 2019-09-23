package com.codeup.volunteertracker.controllers;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

public class PositionController {

    private final PositionRepository positionDao;
    private final EventRepository eventDao;

    public PositionController(PositionRepository positionRepository, EventRepository eventRepository){
        this.positionDao = positionRepository;
        this.eventDao = eventRepository;
    }

//    UNTESTED
    //will have to pass event id as a hidden attribute
    @GetMapping("/events/{id}/create-position")
    public String createPosition(Model viewModel, long id){
        viewModel.addAttribute("position", new Position());
        Event event = eventDao.findOne(id);
        viewModel.addAttribute("event", event);
        return "/position/create";
    }

    // UNTESTED
    @PostMapping("/events/{id}/create-position")
    public String createPosition(@ModelAttribute Position position, @ModelAttribute Event event){
        position.setEvent(event);
        Position savePosition = positionDao.save(position);
        return "redirect:/events/{id}";
    }
}
