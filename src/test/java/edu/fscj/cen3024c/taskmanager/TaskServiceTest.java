
package edu.fscj.cen3024c.taskmanager;

import edu.fscj.cen3024c.taskmanager.dto.TaskDTO;
import edu.fscj.cen3024c.taskmanager.entities.Task;
import edu.fscj.cen3024c.taskmanager.entities.User;
import edu.fscj.cen3024c.taskmanager.exceptions.TaskNotFoundException;
import edu.fscj.cen3024c.taskmanager.repositories.TaskRepository;
import edu.fscj.cen3024c.taskmanager.repositories.UserRepository;
import edu.fscj.cen3024c.taskmanager.services.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1);
        task.setTitle("Complete Module 8");
        task.setDescription("Finish service testing");
        task.setStatus("Pending");
        task.setDueDate("2026-11-30");
    }

    @Test
    void findAll_ShouldReturnAllTasks() {

        when(taskRepository.findAll())
                .thenReturn(java.util.List.of(task));

        var result = taskService.findAll();

        assertEquals(1, result.size());
        assertEquals("Complete Module 8",
                result.get(0).getTitle());

        verify(taskRepository).findAll();
    }

    @Test
    void findById_ShouldReturnTask_WhenExists() {

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));

        TaskDTO result = taskService.findById(1);

        assertNotNull(result);
        assertEquals("Complete Module 8", result.getTitle());

        verify(taskRepository).findById(1);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {

        when(taskRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.findById(999));

        verify(taskRepository).findById(999);
    }

    @Test
    void save_ShouldReturnSavedTask() {

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.save(task);

        assertNotNull(result);
        assertEquals("Complete Module 8", result.getTitle());

        verify(taskRepository).save(task);
    }

    @Test
    void updateTask_ShouldUpdateExistingTask() {

        Task updatedDetails = new Task();
        updatedDetails.setTitle("Updated Module 8");
        updatedDetails.setDescription("Updated description");
        updatedDetails.setStatus("In Progress");
        updatedDetails.setDueDate("2026-12-01");

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task))
                .thenReturn(task);

        TaskDTO result = taskService.updateTask(1, updatedDetails);

        assertEquals("Updated Module 8", result.getTitle());
        assertEquals("Updated description", result.getDescription());
        assertEquals("In Progress", result.getStatus());

        verify(taskRepository).save(task);
    }

    @Test
    void deleteById_ShouldDeleteTask_WhenExists() {

        when(taskRepository.existsById(1)).thenReturn(true);

        taskService.deleteById(1);

        verify(taskRepository).existsById(1);
        verify(taskRepository).deleteById(1);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {

        when(taskRepository.existsById(999)).thenReturn(false);

        assertThrows(TaskNotFoundException.class,
                () -> taskService.deleteById(999));

        verify(taskRepository, never()).deleteById(999);
    }

    @Test
    void assignUserToTask_ShouldAssociateUserWithTask() {

        User user = new User();
        task.setUsers(new HashSet<>());

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(2))
                .thenReturn(Optional.of(user));

        taskService.assignUserToTask(1, 2);

        assertTrue(task.getUsers().contains(user));

        verify(taskRepository).save(task);
    }
}
