package com.codeup.volunteertracker.models;


import javax.persistence.*;
import java.util.Date;

@Table(name="events")
@Entity
public class Event {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Date start;

    @Column(nullable =false)
    private Date stop;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private long  userId;

}
