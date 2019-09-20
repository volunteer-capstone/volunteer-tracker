package com.codeup.volunteertracker.models;

import com.codeup.volunteertracker.models.User;

import javax.persistence.*;
import java.io.Serializable;

@Table(name="user_position")
@Entity
public class UserPosition implements Serializable {

    @Id
    @GeneratedValue
    private long id;


    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name="position_id")
    private Position position;

    @Column
    private boolean isApproved;

    public UserPosition() {
    }

    public UserPosition(User user, Position position, boolean isApproved) {
        this.user = user;
        this.position = position;
        this.isApproved = isApproved;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
