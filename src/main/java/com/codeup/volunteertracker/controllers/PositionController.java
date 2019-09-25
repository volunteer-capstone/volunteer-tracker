package com.codeup.volunteertracker.controllers;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.models.UserPosition;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import com.codeup.volunteertracker.repositories.UserPositionRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PositionController {

    private final PositionRepository positionDao;
    private final EventRepository eventDao;
    private final UserPositionRepository userPositionDao;

    public PositionController(PositionRepository positionRepository, EventRepository eventRepository, UserPositionRepository userPositionRepository){
        this.positionDao = positionRepository;
        this.eventDao = eventRepository;
        this.userPositionDao = userPositionRepository;
    }

//    UNTESTED-- create
    //will have to pass event id as a hidden attribute
    @GetMapping("/events/{id}/create-position")
    public String createPosition(Model viewModel, @PathVariable long id){
        viewModel.addAttribute("position", new Position());
        Event event = eventDao.findOne(id);
        viewModel.addAttribute("event", event);
        UserPosition userPosition = new UserPosition();
        viewModel.addAttribute("userPosition", userPosition);
        return "events/create-position";
    }

    // UNTESTED-- create(go back and wrap create event and post with try catch for the date parse)
    @PostMapping("/events/{id}/create-position")
    public String createPosition(@PathVariable long id, @ModelAttribute Event event, @ModelAttribute UserPosition userPosition, @RequestParam(name="description") String description, @RequestParam(name="start") String start, @RequestParam(name="end") String end, @RequestParam(name="numNeeded") int numNeeded, @RequestParam(name="title") String title) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date localTimeObj1= df.parse(start);
        Date localTimeObj2 = df.parse(end);
        Position position = new Position();
        position.setEvent(event);
        position.setDescription(description);
        position.setStart(localTimeObj1);
        position.setEnd(localTimeObj2);
        position.setNumNeeded(numNeeded);
        position.setTitle(title);
        Position savePosition = positionDao.save(position);
        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userPosition.setPosition(savePosition);
        userPosition.setUser(userSession);
        UserPosition saveUserPosition = userPositionDao.save(userPosition);
        return "redirect:/events/" + savePosition.getEvent().getId();
    }

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
