package com.time_tracker.be.repository;

import com.time_tracker.be.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    // Custom query methods can be defined here if needed
    UserModel findByEmail(String email);
}
