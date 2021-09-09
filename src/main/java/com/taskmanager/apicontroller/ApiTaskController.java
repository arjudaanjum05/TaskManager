package com.taskmanager.apicontroller;

import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ApiTaskController {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskServiceImpl taskService;

    @PostMapping("/tasks")
    public HashMap<String, String> saveProject(@RequestBody Task task) {
        taskRepository.save(task);
        HashMap<String, String> return_message = new HashMap<String, String>();
        return_message.put("message", "Task Created Successfully");
        return return_message;
    }

    @GetMapping("/tasks")
    public List<Task> taskList(@Param("keyword") String keyword, @Param("user_id") Long user_id, @Param("expired") String expired, @Param("completed") String status) {

        if (keyword != null) {
            return taskRepository.findAll(keyword);
        }

        if (status != null) {
            List<Task> tasks = new ArrayList<>();
            for (Task task : taskRepository.findAll()) {
                if (task.isCompleted()) {
                    tasks.add(task);
                }
            }
            return tasks;
        }

        return taskRepository.findAll();
    }

    @GetMapping("/tasks/{id}")
    public Task GetTask(@PathVariable("id") Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @PutMapping("/tasks/{id}")
    public HashMap<String, String> updateTask(@RequestBody Task task, @PathVariable("id") Long id) {
        taskService.updateTask(id, task);
        HashMap<String, String> return_message = new HashMap<String, String>();
        return_message.put("message", "Task updated Successfully");
        return return_message;
    }
}
