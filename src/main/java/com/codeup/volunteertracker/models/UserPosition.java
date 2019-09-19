package com.codeup.volunteertracker.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="user_position")
@Entity
public class UserPosition implements Serializable {

    @Id
    @ManyToMany
    private User user;

    @Id
    @ManyToMany
    private Position position;

    private boolean isApproved;

//    public UserPosition(User user, Position position, boolean isApproved) {
//        this.user = user;
//        this.position = position;
//        this.isApproved = isApproved;
//    }

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
