package com.codeup.volunteertracker.repositories;

import com.codeup.volunteertracker.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends CrudRepository<Position, Long> {

    @Query(value ="select event_id from volunteer_db.positions where id = ?", nativeQuery = true)
    long positionEventId(long id);

    List<Position> findAllByEvent_Id(long eventId);


}
