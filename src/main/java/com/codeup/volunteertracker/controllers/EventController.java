package com.codeup.volunteertracker.controllers;


import com.codeup.volunteertracker.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventController {

//    starting dependency injection.....
//    private final UserRepository userDao;
//
//    public UserController(UserRepository userRepository){
//        this.userDao = userRepository;
//    }

    @GetMapping("/events")
    public String eventIndex(){
//        code to iterate event list will go here
        return "events/index";
    }


}
