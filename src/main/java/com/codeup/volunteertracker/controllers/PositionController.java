package com.codeup.volunteertracker.controllers;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        return "events/create-position";
    }

//    // UNTESTED-- create
//    @PostMapping("/events/{id}/create-position")
//    public String createPosition(@ModelAttribute Position position, @ModelAttribute Event event){
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//        Date localTimeObj1= df.parse(start);
//        Date localTimeObj2 = df.parse(end);
//        position.setEvent(event);
//        Position savePosition = positionDao.save(position);
//        return "redirect:/events/" + savePosition.getEvent().getId();
//    }

//    UNTESTED -- EDIT
    @GetMapping("/events/positions/edit/{id}")
   public String editPosition(@PathVariable long id, Model viewModel){
        viewModel.addAttribute("position", positionDao.findOne(id));
        return "positions/edit";
    }

//    UNTESTED -- EDIT
    @PostMapping("/events/positions/edit/{id}")
    public String editPosition(@PathVariable long id, @RequestParam(name="title") String title, @RequestParam(name="description") String description, @RequestParam(name="numNeeded") int numNeeded, @RequestParam(name="start") Date start, @RequestParam(name="end") Date end, Model viewModel) {
        Position editedPosition = positionDao.findOne(id);
        editedPosition.setTitle(title);
        editedPosition.setDescription(description);
        editedPosition.setNumNeeded(numNeeded);
        editedPosition.setStart(start);
        editedPosition.setEnd(end);
        positionDao.save(editedPosition);
        long eventId = positionDao.positionEventId(id);
        return "redirect:/events/" + eventId;
    }

//    UNTESTED -- DELETE
    // unsure if this will work appropriately
    @GetMapping("/events/positions/delete/{id}")
    public String deletePosition(@PathVariable long id){
        Position toDelete = positionDao.findOne(id);
        long eventId = toDelete.getEvent().getId();
        return "redirect:/events/" + eventId;
    }

//    UNTESTED -- DELETE
    @PostMapping("/events/positions/delete/{id}")
    public String afterDelete(@PathVariable long id){
        Position toDelete = positionDao.findOne(id);
        long eventId =toDelete.getEvent().getId();
        positionDao.delete(id);
        return "redirect:/events/" + eventId ;
    }

}
