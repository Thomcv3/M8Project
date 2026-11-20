
package edu.fscj.cen3024c.taskmanager.repositories;

import edu.fscj.cen3024c.taskmanager.entities.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryMockTest {

    @Mock
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("Complete Module 8");
        task.setDescription("Complete repository testing");
        task.setStatus("Pending");
        task.setDueDate("2026-11-30");
    }

    @Test
    void saveTask_ShouldReturnSavedTask() {

        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask);
        assertEquals("Complete Module 8", savedTask.getTitle());

        verify(taskRepository).save(task);
    }

    @Test
    void findById_ShouldReturnTask_WhenExists() {

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));

        Optional<Task> found = taskRepository.findById(1);

        assertTrue(found.isPresent());
        assertEquals("Complete Module 8",
                found.get().getTitle());

        verify(taskRepository).findById(1);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {

        when(taskRepository.findById(999))
                .thenReturn(Optional.empty());

        Optional<Task> found = taskRepository.findById(999);

        assertTrue(found.isEmpty());

        verify(taskRepository).findById(999);
    }

    @Test
    void deleteById_ShouldCallRepository() {

        doNothing().when(taskRepository).deleteById(1);

        taskRepository.deleteById(1);

        verify(taskRepository).deleteById(1);
    }
}
