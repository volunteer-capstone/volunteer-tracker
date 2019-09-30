package com.codeup.volunteertracker.controllers;

import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.repositories.UserRepository;
import com.codeup.volunteertracker.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {
    private final UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Autowired
    private EmailService emailService;

    //    CREATE USER
    @GetMapping("/register")
    public String viewRegister(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User registerUser, Errors validation, Model model, @RequestParam(name = "organizer", defaultValue = "off") String isOrganizer) {
        if(userRepo.countAllByEmailOrUsername(registerUser.getEmail(), registerUser.getUsername()) > 0) {
            validation.rejectValue(
                    "username",
                    "user.username",
                    "Invalid username and/or email."
            );
        }
        if(!isOrganizer.equals("off")) {
            registerUser.setOrganizer(true);
        }
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("user", registerUser);
            return "users/register";
        } else {
            String hash = passwordEncoder.encode(registerUser.getPassword());
            registerUser.setPassword(hash);
            userRepo.save(registerUser);
            emailService.createdAnAccount(registerUser, "Account Created with Path of the Volunteer", String.format("Congratulations! An account was made at pathofthevolunteer.com using this email address under the username: %s.  Enjoy volunteering and giving back to your community! If you feel that this has occurred in error please visit our website to contact us.", registerUser.getUsername())) ;

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
            viewModel.addAttribute("curUser", userSession);
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
            emailService.createdAnAccount(editedUser, "Account Changes with Path of the Volunteer", String.format("Notification: Changes were made to your account.  If you feel that you are receiving this email in error, please visit pathofthevolunteer.com to contact us.")) ;
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
        emailService.createdAnAccount(user, "Account Deleted with Path of the Volunteer", String.format("Thank you, %s %s for being a member of pathofthevolunteer.com . If you would like to use our services again in the future, please feel free to visit our site again.", user.getFirstName(), user.getLastName())) ;

        return "redirect:/login?logout";
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
