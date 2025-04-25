package com.vijayi.TaskManager.Repository;

import java.time.Instant;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vijayi.TaskManager.Entity.Task;
import com.vijayi.TaskManager.Entity.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query("SELECT t FROM Task t WHERE t.isDeleted = false")
    List<Task> findAllActiveTasks();
    
    @Query("SELECT t FROM Task t WHERE t.isDeleted = true")
    List<Task> findAllDeletedTasks();
    
    boolean existsByTitleAndExpectedEndDateTimeAndIsDeletedFalse(String title, Instant expectedEndDateTime);
    
    @Modifying
    @Query("UPDATE Task t SET t.isDeleted = true WHERE t.id = :id")
    void softDelete(@Param("id") Long id);
    
    @Query("SELECT t FROM Task t WHERE t.status = :status AND t.isDeleted = false")
    List<Task> findByStatus(@Param("status") TaskStatus status);
}
