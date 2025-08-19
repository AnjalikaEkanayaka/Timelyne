package com.Timelyne.repository; // 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Timelyne.model.User; // 

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom finder to get user by email (for login)
    User findByEmail(String email);
}
