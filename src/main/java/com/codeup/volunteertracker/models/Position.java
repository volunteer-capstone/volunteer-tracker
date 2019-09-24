package com.codeup.volunteertracker.models;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Positions must have a title")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Positions must have a description")
    private String description;

    @Column(nullable = false)
    @NotBlank(message = "Please enter how many are needed for this position")
    private int numNeeded;

    @Column(nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @NotBlank(message = "Enter a start time/date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm")
    private Date start;

    @Column(nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @NotBlank(message = "Enter an end time/date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm")
    private Date end;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany
    private List<UserPosition> positionVolunteers;

    public Position(){}

    public Position(String title, String description, int numNeeded, Date start, Date end, Event event, List<UserPosition> positionVolunteers) {
        this.title = title;
        this.description = description;
        this.numNeeded = numNeeded;
        this.start = start;
        this.end = end;
        this.event = event;
        this.positionVolunteers = positionVolunteers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumNeeded() {
        return numNeeded;
    }

    public void setNumNeeded(int numNeeded) {
        this.numNeeded = numNeeded;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<UserPosition> getPositionVolunteers() {
        return positionVolunteers;
    }

    public void setPositionVolunteers(List<UserPosition> positionVolunteers) {
        this.positionVolunteers = positionVolunteers;
    }
}
