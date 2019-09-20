package com.codeup.volunteertracker.repositories;

import com.codeup.volunteertracker.models.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<Position, Long> {
}
