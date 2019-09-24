package com.codeup.volunteertracker.repositories;

import com.codeup.volunteertracker.models.Position;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<Position, Long> {

    @Query(value ="select event_id from volunteer_db.positions where id = ?", nativeQuery = true)
    long positionEventId(long id);


}
