package com.codeup.volunteertracker.repositories;

import com.codeup.volunteertracker.models.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
}