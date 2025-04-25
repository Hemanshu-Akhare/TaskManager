package com.vijayi.TaskManager.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vijayi.TaskManager.Entity.Task;
import com.vijayi.TaskManager.Entity.TaskStatus;
import com.vijayi.TaskManager.Repository.TaskRepository;

import jakarta.transaction.Transactional;
@Service
public class TaskService {

	 @Autowired
	    private TaskRepository taskRepository;
	    
	    @Transactional
	    public Task createTask(Task task) {
	        // Validate mandatory fields for IN_PROGRESS status
	        if (task.getStatus() == TaskStatus.IN_PROGRESS) {
	            if (task.getExpectedStartDateTime() == null || task.getExpectedEndDateTime() == null) {
	                throw new IllegalArgumentException("Expected start and end dates are mandatory for tasks in progress");
	            }
	        }
	        
	        // Check for duplicate task
	        if (taskRepository.existsByTitleAndExpectedEndDateTimeAndIsDeletedFalse(
	                task.getTitle(), task.getExpectedEndDateTime())) {
	            throw new IllegalArgumentException("A task with the same title and end date already exists");
	        }
	        
	        return taskRepository.save(task);
	    }
	    
	    public List<Task> getAllTasks() {
	        return taskRepository.findAllActiveTasks();
	    }
	    
	    public Task getTaskById(Long id) {
	        return taskRepository.findById(id)
	                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
	    }
	    
	    @Transactional
	    public Task updateTask(Long id, Task taskDetails) {
	        Task task = getTaskById(id);
	        
	        if (task.isDeleted()) {
	            throw new IllegalStateException("Cannot update a deleted task");
	        }
	        
	        // Validate status transition
	        if (taskDetails.getStatus() == TaskStatus.IN_PROGRESS) {
	            if (taskDetails.getExpectedStartDateTime() == null || 
	                taskDetails.getExpectedEndDateTime() == null) {
	                throw new IllegalArgumentException("Expected dates are required for IN_PROGRESS status");
	            }
	        }
	        
	        task.setTitle(taskDetails.getTitle());
	        task.setDescription(taskDetails.getDescription());
	        task.setStatus(taskDetails.getStatus());
	        task.setExpectedStartDateTime(taskDetails.getExpectedStartDateTime());
	        task.setExpectedEndDateTime(taskDetails.getExpectedEndDateTime());
	        
	        return taskRepository.save(task);
	    }
	    
	    @Transactional
	    public void deleteTask(Long id, boolean confirm) {
	        if (!confirm) {
	            throw new IllegalArgumentException("Deletion must be confirmed");
	        }
	        
	        Task task = getTaskById(id);
	        if (task.isDeleted()) {
	            throw new IllegalStateException("Task is already deleted");
	        }
	        
	        taskRepository.softDelete(id);
	    }
	    
	    public List<Task> getAllDeletedTasks() {
	        return taskRepository.findAllDeletedTasks();
	    }
	    
	    public List<Task> getTasksByStatus(String status) {
	        try {
	            TaskStatus taskStatus = TaskStatus.fromString(status);
	            return taskRepository.findByStatus(taskStatus);
	        } catch (IllegalArgumentException e) {
	            throw new IllegalArgumentException("Invalid status value. Allowed values: Pending, In Progress, Completed");
	        }
	    }
}
