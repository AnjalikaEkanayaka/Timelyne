package com.Timelyne.controller;

import com.Timelyne.dto.CoreTaskDTO;
import com.Timelyne.model.Task;
import com.Timelyne.service.CoreTaskService;
import com.Timelyne.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List; 

@RestController
@RequestMapping("/api/tasks")
public class CoreTaskController {

    @Autowired
    private CoreTaskService coreTaskService;

    @Autowired
    private JwtUtil jwtUtil;

    //  Endpoint to create a new core task
    @PostMapping("/core")
    public Task createCoreTask(@RequestBody CoreTaskDTO taskDto,
                               @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7)); // remove "Bearer "
        return coreTaskService.createCoreTask(taskDto, email);
    }

    // Endpoint to get all core tasks for current user
    @GetMapping("/core")
    public List<CoreTaskDTO> getMyTasks(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String email = jwtUtil.extractEmail(jwt);
        return coreTaskService.getTasksForUser(email);
    }

    //Update a core work

    @PutMapping("/core/{taskId}")
    public Task updateCoreTask(@PathVariable Long taskId,
                               @RequestBody CoreTaskDTO taskDto,
                               @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        return coreTaskService.updateCoreTask(taskId,taskDto,email); // remove "Bearer "
}

//Delete a core task
    @DeleteMapping("core/{taskId}")
    public String deleteCoreTask(@PathVariable Long taskId,
                                 @RequestHeader("Authorization") String token) {

                                 String email=jwtUtil.extractEmail(token.substring(7));
                                 coreTaskService.deleteCoretask(taskId, email);
                                 return "Task deleted successfully";
}
}