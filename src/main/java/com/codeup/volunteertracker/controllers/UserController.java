package com.codeup.volunteertracker.controllers;

import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UserController {
    private final UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    //    CREATE USER
    @GetMapping("/register")
    public String viewRegister(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User registerUser, Errors validation, Model model) {
        if(userRepo.countAllByEmailOrUsername(registerUser.getEmail(), registerUser.getUsername()) > 0) {
            validation.rejectValue(
                    "username",
                    "user.username",
                    "Invalid username and/or email."
            );
        }
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("user", registerUser);
            return "users/register";
        } else {
            String hash = passwordEncoder.encode(registerUser.getPassword());
            registerUser.setPassword(hash);
            userRepo.save(registerUser);
            return "redirect:/login";
        }
    }

      @GetMapping("/users/{id}/profile")
    public String show(@PathVariable long id, Model viewModel) {

//        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        long usId = userSession.getId();

        User user = userRepo.findOne(id);
        viewModel.addAttribute("user", user);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            String userId = "";
            viewModel.addAttribute("userId", userId);
        } else {
            User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            long userId = userSession.getId();
            viewModel.addAttribute("userId", userId);
        }

        return "users/view";
    }
  
  
//    EDIT USER
    @GetMapping("/profile/edit")
    public String editProfile(Model viewModel){
        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = userSession.getId();
//        User user = userRepo.findOne(id);
        viewModel.addAttribute("user", userSession);
        return "users/edit";
    }

//    WILL HAVE TO GO BACK AND ADD ABILITY TO EDIT PHOTO
    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute User user, @RequestParam(name="email") String email, @RequestParam(name="firstName") String firstName, @RequestParam(name="lastName") String lastName, @RequestParam(name="phoneNumber") String phoneNumber, @RequestParam(name="username") String username, @RequestParam(name="file") String photo, @RequestParam(name="bio") String bio){
        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setId(userSession.getId());

//        user.setPassword(hash);
        if (BCrypt.checkpw(user.getPassword(),userSession.getPassword())) {
            user.setEmail(email);
            user.setHours(userSession.getHours());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.setUsername(username);
            user.setPassword(userSession.getPassword());
            user.setPhoto(photo);
            user.setBio(bio);
            User editedUser = userRepo.save(user);
            return "redirect:/users/" + user.getId() + "/profile";
        } else {
            return "redirect:/profile/edit";
        }
    }

//  DELETE USER

    @GetMapping("profile/delete")
    public String deleteProfile(Model viewModel){
        User userSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = userSession.getId();
        User user = userRepo.findOne(userId);
        userRepo.delete(userId);

        return "redirect:/login";
    }

//    MAKE USER AN ORGANIZER
    @PostMapping("profile/organizer/{id}")
    public String makeOrganizer(@PathVariable long id) {
        User user = userRepo.findOne(id);
        user.setOrganizer(true);
        userRepo.save(user);
        return "redirect:/users/"+ user.getId() + "/profile";
    }
}
