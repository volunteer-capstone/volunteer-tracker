package com.codeup.volunteertracker.models;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.UserPosition;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id @GeneratedValue
    private long id;

    @Column (nullable = false)
    private String firstName;

    @Column (nullable = false)
    private String lastName;

    @Column (nullable = false, length = 20)
    private String phoneNumber;

    @Column (nullable = false)
    private String email;

    @Column (nullable = false)
    private String password;

    @Column
    private long hours;

    @Column (nullable = false)
    private boolean isOrganizer;

    @Column
    private String photo;

    @OneToMany(cascade =CascadeType.ALL, mappedBy = "creator")
    private List<Event> events;


    @OneToMany(mappedBy = "user")
    private List<UserPosition> userPosition;

    public User() {
    }

    public User(String firstName, String lastName, String phoneNumber, String email, String password, long hours, boolean isOrganizer, String photo, List<Event> events, List<UserPosition> userPosition) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.hours = hours;
        this.isOrganizer = isOrganizer;
        this.photo = photo;
        this.events = events;
        this.userPosition = userPosition;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

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
}
