package com.codeup.volunteertracker.repositories;

import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.models.UserPosition;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserPositionRepository extends CrudRepository<UserPosition, Long>{

    List<UserPosition> findByPosition_Id(long positionId);

    List<UserPosition> findByUser(User user);
}
