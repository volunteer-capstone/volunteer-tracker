package com.codeup.volunteertracker.repositories;

import com.codeup.volunteertracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    int countAllByEmailOrUsername(String email, String username);

    long getByEvents(long id);
}
