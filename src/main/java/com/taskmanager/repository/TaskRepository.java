package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwnerOrderByDateDesc(User user);

    @Query("SELECT p FROM Task p WHERE CONCAT(p.name, p.owner, p.project, p.isCompleted) LIKE %?1%")
    public List<Task> findAll(String keyword);
}