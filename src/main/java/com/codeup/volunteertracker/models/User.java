package com.codeup.volunteertracker.models;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.UserPosition;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private long id;

    @Column (nullable = false)
    @NotBlank(message = "Please enter your first name")
    private String firstName;

    @Column (nullable = false)
    @NotBlank(message = "Please enter your last name")
    private String lastName;

    @Column (nullable = false, length = 20)
    @NotBlank(message = "Please enter a phone number")
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Please enter a username")
    private String username;

    @Column (nullable = false, unique = true)
    @NotBlank(message = "Please enter an email")
    @Email(message = "Email should be valid")
    private String email;

    @Column (nullable = false)
    @NotBlank(message = "Please enter a password")
    private String password;

    @Column
    private long hours;

    @Column (nullable = true)
    private boolean isOrganizer;

    @Column
    private String photo;

    @OneToMany(cascade =CascadeType.ALL, mappedBy = "creator")
    private List<Event> events;


    @OneToMany(mappedBy = "user")
    private List<UserPosition> userPosition;

    public User() {
    }

    public User(String firstName, String lastName, String phoneNumber, String username, String email, String password, long hours, boolean isOrganizer, String photo, List<Event> events, List<UserPosition> userPosition) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.email = email;
        this.password = password;
        this.hours = hours;
        this.isOrganizer = isOrganizer;
        this.photo = photo;
        this.events = events;
        this.userPosition = userPosition;
    }


    public User(User copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        firstName = copy.firstName;
        lastName = copy.lastName;
        phoneNumber = copy.phoneNumber;
        username = copy.username;
        email = copy.email;
        password = copy.password;
        hours = copy.hours;
        isOrganizer = copy.isOrganizer;
        photo = copy.photo;
        events = copy.events;
        userPosition = copy.userPosition;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}


    public long getHours() {return hours;}

    public void setHours(long hours) {this.hours = hours;}

    public boolean isOrganizer() {return isOrganizer;}

    public void setOrganizer(boolean organizer) {isOrganizer = organizer;}

    public String getPhoto() {return photo;}

    public void setPhoto(String photo) {this.photo = photo;}

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<UserPosition> getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(List<UserPosition> userPosition) {
        this.userPosition = userPosition;
    }

//    public List<String> getUserPositionTitles() {
//        List<UserPosition> posList = userPosition;
//        List<String> positions = new ArrayList<>();
//        for (int i = 0; i < posList.size(); i++) {
//            positions.add(posList.get(i).getPosition().getTitle());
//        }
//        return positions;
//    }

    public List<Event> getUserEvents() {

        List<UserPosition> posList = userPosition;
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < posList.size(); i++) {
            Event nextEvent = (posList.get(i).getPosition().getEvent());
            if (!events.contains(nextEvent)) {
                events.add(posList.get(i).getPosition().getEvent());
            }
        }
        return events;
    }

    public List<Position> getUserPositionTitlesByEventId(Long id) {
        List<UserPosition> posList = userPosition;
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < posList.size(); i++) {
            if (posList.get(i).getPosition().getEvent().getId() == id) {
                positions.add(posList.get(i).getPosition());
            }
        }
        return positions;
    }
}
