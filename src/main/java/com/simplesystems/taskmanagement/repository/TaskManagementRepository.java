package com.simplesystems.taskmanagement.repository;

import com.simplesystems.taskmanagement.entity.Task;
import com.simplesystems.taskmanagement.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskManagementRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByDueDateBeforeAndStatus(LocalDate dueDate, TaskStatus status);
}
