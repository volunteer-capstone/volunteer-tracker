package com.codeup.volunteertracker.models;

import com.codeup.volunteertracker.models.User;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name="events")
@Entity
public class Event {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

   @Column(nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stop;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User  creator;

    @OneToMany(cascade =CascadeType.ALL, mappedBy = "event")
    private List <Position> positions;

    public Event() {
    }

    public Event(String title, String description, String location, Date start, Date stop, User creator, List<Position> positions) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.stop = stop;
        this.creator = creator;
        this.positions= positions;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
