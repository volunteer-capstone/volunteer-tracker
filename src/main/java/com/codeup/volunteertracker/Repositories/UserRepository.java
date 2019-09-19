package com.codeup.volunteertracker.Repositories;

import com.codeup.volunteertracker.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
