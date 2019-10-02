package com.codeup.volunteertracker.repositories;

import com.codeup.volunteertracker.models.Position;
import com.codeup.volunteertracker.models.User;
import com.codeup.volunteertracker.models.UserPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserPositionRepository extends CrudRepository<UserPosition, Long>{

    List<UserPosition> findAllByPosition_Id(long positionId);

    List<UserPosition> findByUser(User user);

    List<UserPosition> findAllByPosition(Position position);

//    UserPosition findByPosition_Id(long positionId);
//    @Query(value="select id from user_position where position_id = ? and user_id = ?", nativeQuery = true)
    UserPosition findUserPositionByPositionAndUser(Position position, User user);
}

