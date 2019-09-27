package com.codeup.volunteertracker.models;

import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name="events")
public class Event {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Please give your event a title")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Please give your event a description")
    private String description;

    @NotBlank(message = "Please enter a location for your event")
    private String location;

    @Column(nullable = false)
    @NotBlank(message = "Please enter an address for your event.")
    private String address;

    @Column(nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

   @Column(nullable = false, columnDefinition="DATETIME")
   @Temporal(TemporalType.TIMESTAMP)
   private Date stop;

   @Column( columnDefinition = "TEXT")
   private String photo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @OneToMany(cascade =CascadeType.ALL, mappedBy = "event")
    private List <Position> positions;

    public Event() {
    }

    public Event(String title, String description, String location, Date start, Date stop, User creator, List<Position> positions, String photo, String address) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.stop = stop;
        this.creator = creator;
        this.positions= positions;
        this.photo= photo;
        this.address = address;
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

    public long posHours(Date start, Date end) {
        List<Position> posList = positions;
        long totalHours = 0;
        for (int i = 0; i < posList.size(); i++) {
            long diffInMillis = Math.abs(end.getTime() - start.getTime());
            long diff = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            totalHours = diff;
        }
        return totalHours;
    }
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
