package com.Timelyne.service;

import com.Timelyne.dto.CoreTaskDTO;
import com.Timelyne.model.Task;
import com.Timelyne.model.User;
import com.Timelyne.repository.CoreTaskRepository;
import com.Timelyne.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoreTaskService {

    @Autowired
    private CoreTaskRepository coreTaskRepository;

    @Autowired
    private UserRepository userRepository;

    //  CREATE TASK
    public Task createCoreTask(CoreTaskDTO dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found for email: " + userEmail);
        }

        //  Validate minutes: only 0, 15, 30, or 45
        if (dto.getMinutesRequired() % 15 != 0) {
            throw new IllegalArgumentException("Minutes must be 0, 15, 30, or 45");
        }

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setHoursRequired(dto.getHoursRequired());
        task.setMinutesRequired(dto.getMinutesRequired());
        task.setMinutesDone(0);
        task.setDeadline(dto.getDeadline());
        task.setUser(user);

        // Optional: handle subtasks
        if (dto.getParentTaskId() != null) {
            Optional<Task> parentTaskOpt = coreTaskRepository.findById(dto.getParentTaskId());
            parentTaskOpt.ifPresent(task::setParentTask);
        }

        return coreTaskRepository.save(task);
    }

    //  GET TASKS FOR CURRENT USER
    public List<CoreTaskDTO> getTasksForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) throw new RuntimeException("User not found");

        List<Task> tasks = coreTaskRepository.findByUser(user);

        return tasks.stream().map(task -> new CoreTaskDTO(
                task.getTitle(),
                task.getDescription(),
                task.getHoursRequired(),
                task.getMinutesRequired(),
                task.getDeadline(),
                task.getParentTask() != null ? task.getParentTask().getId() : null
        )).toList();
    }
    //Update core Task
    public Task updateCoreTask(Long taskId,CoreTaskDTO dto, String userEmail){

        User user=userRepository.findByEmail(userEmail);
        if(user==null){
            throw new RuntimeException("User not found for email: ");

            
        }

        Task task=coreTaskRepository.findById(taskId)
                 .orElseThrow(()->new RuntimeException("Task not found"));

        if(!task.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Task does not belong to the user");
        }  
        
        if(dto.getMinutesRequired()% 15 !=0){
            throw new IllegalArgumentException("Minutes must be 0, 15, 30, or 45");
        }

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setHoursRequired(dto.getHoursRequired());
        task.setMinutesRequired(dto.getMinutesRequired());
        task.setDeadline(dto.getDeadline());

        if(dto.getParentTaskId()!=null){

            Optional<Task> parentTaskOpt=coreTaskRepository.findById(dto.getParentTaskId());
            parentTaskOpt.ifPresent(task::setParentTask);
        }else{
            task.setParentTask(null);
        }

        return coreTaskRepository.save(task);

        }

        //Delete Core task
        public void deleteCoretask(Long taskid,String userEmail){
            User user=userRepository.findByEmail(userEmail);
            if(user==null){
                throw new RuntimeException("User not found for email: " + userEmail);
                }

                Optional<Task> taskOpt=coreTaskRepository.findById(taskid);

                if(taskOpt.isEmpty()){
                    throw new RuntimeException("Task not found with id: " + taskid);
                }

                Task task =taskOpt.get();


                if(!task.getUser().getId().equals(user.getId())){
                    throw new RuntimeException("Task does not belong to the user");
                } 


                coreTaskRepository.delete(task);
        }

    }

