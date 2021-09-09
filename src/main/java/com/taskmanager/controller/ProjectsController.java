package com.taskmanager.controller;


import com.taskmanager.model.Project;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ProjectsController {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/projects/create")
    public String showEmptyProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        return "forms/projects-new";
    }

    @PostMapping("/projects/create")
    public String createTask(@Valid Project project, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "forms/projects-new";
        }

        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        project.setUser(user);

        projectRepository.save(project);

        return "redirect:/projects";
    }

    @GetMapping("/projects")
    public String listTasks(Model model, SecurityContextHolderAwareRequestWrapper request) {
        model.addAttribute("projects", projectRepository.findAll());
        return "views/projects";
    }


    @GetMapping("/projects/delete/{id}")
    public String DeleteProject(@PathVariable int id) {
        projectRepository.deleteById(id);
        return "redirect:/projects";
    }

    @GetMapping("/projects/{id}/tasks")
    public String listTasks(Model model, @PathVariable("id") int id, Principal principal, SecurityContextHolderAwareRequestWrapper request) {

        prepareTasksListModel(model, principal, request, id);
        model.addAttribute("onlyInProgress", true);
        return "views/projects-task";
    }

    private void prepareTasksListModel(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request, int id) {
        String email = principal.getName();
        User signedUser = userService.getUserByEmail(email);
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");
        Project project = projectRepository.findById(id).orElse(null);

        model.addAttribute("tasks", project.getTasks());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }
}