package com.codeup.volunteertracker.controllers;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.repositories.EventRepository;
import com.codeup.volunteertracker.repositories.PositionRepository;
import com.codeup.volunteertracker.repositories.UserPositionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private final PositionRepository positionDao;
    private final EventRepository eventDao;
    private final UserPositionRepository userPositionDao;

    public HomeController(PositionRepository positionRepository, EventRepository eventRepository, UserPositionRepository userPositionRepository){
        this.positionDao = positionRepository;
        this.eventDao = eventRepository;
        this.userPositionDao = userPositionRepository;
    }

    @GetMapping("/")
    public String homePage(Model viewModel){
        List<Event> eventList = (List<Event>) eventDao.findAll();
        List<String> orgList = new ArrayList<>();
        for (int i = 0; i < eventList.size(); i++) {
            orgList.add(eventList.get(i).getOrganization());
        }
        Collections.sort(orgList);
        viewModel.addAttribute("orgList", orgList);
        return "home";
    }

    @GetMapping("/about-us")
    public String aboutUsPage(){
        return "about-us";
    }
}
