package com.codeup.volunteertracker.controllers;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.models.UserPosition;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import com.codeup.volunteertracker.repositories.UserPositionRepository;
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
public class PositionController {

    private final PositionRepository positionDao;
    private final EventRepository eventDao;
    private final UserPositionRepository userPositionDao;

    public PositionController(PositionRepository positionRepository, EventRepository eventRepository, UserPositionRepository userPositionRepository){
        this.positionDao = positionRepository;
        this.eventDao = eventRepository;
        this.userPositionDao = userPositionRepository;
    }

    @GetMapping("events/{id}/create-position")
    public String createPosition(Model viewModel, @PathVariable long id){
        viewModel.addAttribute("position", new Position());
        Event event = eventDao.findOne(id);
        viewModel.addAttribute("event", event);
        return "events/create-position";
    }

    //  create(go back and wrap create event and post with try catch for the date parse)
    @PostMapping("events/{id}/create-position")
    public String createPosition(@PathVariable long id, @RequestParam(name="description") String description, @RequestParam(name="start") String start, @RequestParam(name="end") String end, @RequestParam(name="numNeeded") int numNeeded, @RequestParam(name="title") String title) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date starttime= df.parse(start);
        Date endtime = df.parse(end);
        System.out.println("positions info:");
        Position position = new Position();
        System.out.println(description);
        position.setDescription(description);
        position.setStart(starttime);
        position.setEnd(endtime);
        position.setNumNeeded(numNeeded);
        position.setTitle(title);
        Event event = eventDao.findOne(id);
        position.setEvent(event);
        Position savePosition = positionDao.save(position);
        System.out.println(savePosition.getEvent());
        return "redirect:/events/" + savePosition.getEvent().getId();
    }

    // EDIT POSITION
    @GetMapping("/events/positions/edit/{id}")
   public String editPosition(@PathVariable long id, Model viewModel){
        Position position = positionDao.findOne(id);
        viewModel.addAttribute("position", position);
        System.out.println(position.getTitle());
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

//    UNTESTED -- DELETE
    // unsure if this will work appropriately
    @GetMapping("/events/positions/delete/{id}")
    public String deletePosition(@PathVariable long id){
        Position toDelete = positionDao.findOne(id);
        List<UserPosition> userPositions = userPositionDao.findByPosition_Id(toDelete.getId());
        for (UserPosition userPosition : userPositions){
            long userPositionId = userPosition.getId();
            userPositionDao.delete(userPositionId);
        }
        long eventId = toDelete.getEvent().getId();
        positionDao.delete(toDelete);
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
