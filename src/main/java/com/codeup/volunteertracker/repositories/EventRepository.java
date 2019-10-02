package com.codeup.volunteertracker.repositories;

import com.codeup.volunteertracker.models.Event;
import com.codeup.volunteertracker.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    @Query(value ="select user_id from volunteer_db.events where id = ?", nativeQuery = true)
    long eventCreatorId(long id);

    List<Event> findAllByCreator(User user);
}