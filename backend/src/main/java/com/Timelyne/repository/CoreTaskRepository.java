package com.Timelyne.repository;
import com.Timelyne.model.User;


import com.Timelyne.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreTaskRepository extends JpaRepository<Task, Long> {

    //  Custom method: Find all tasks by a user's ID
    List<Task> findByUserId(Long userId);
    List<Task> findByUser(User user);


    
}
