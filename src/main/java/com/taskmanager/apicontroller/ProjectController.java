package com.taskmanager.apicontroller;


import com.taskmanager.model.Project;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/projects")
    public HashMap<String, String> saveProject(@RequestBody Project project, @Param("user_id") Long user_id) {

        if (user_id != null) {
            User user = userRepository.findById(user_id).orElse(null);
            project.setUser(user);
        }

        projectRepository.save(project);
        HashMap<String, String> return_message = new HashMap<String, String>();
        return_message.put("message", "Project Created Successfully");
        return return_message;
    }

    @GetMapping("/projects")
    public List<Project> projectList(@Param("user_id") Long user_id) {

        if (user_id != null) {
            List<Project> projects = new ArrayList<>();
            for (Project project : projectRepository.findAll()) {
                if (project.getUser().getId() == user_id) {
                    projects.add(project);
                }
            }

            return projects;
        }

        return projectRepository.findAll();
    }

    @DeleteMapping("/projects/{id}")
    public HashMap<String, String> DeleteProject(@PathVariable int id) {
        projectRepository.deleteById(id);
        HashMap<String, String> return_message = new HashMap<String, String>();
        return_message.put("message", "Project deleted Successfully.");
        return return_message;
    }

}
