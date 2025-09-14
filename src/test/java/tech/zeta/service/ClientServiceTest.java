package tech.zeta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.zeta.model.Payment;
import tech.zeta.model.Project;
import tech.zeta.model.Task;
import tech.zeta.repository.PaymentRepository;
import tech.zeta.repository.ProjectRepository;
import tech.zeta.repository.TaskRepository;
import tech.zeta.util.Param;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClientServiceTest {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private PaymentRepository paymentRepository;
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        paymentRepository = Mockito.mock(PaymentRepository.class);

        clientService = new ClientService();
        // Inject mocks
        clientService.setTaskRepository(taskRepository) ;
        clientService.setProjectRepository(projectRepository);
        clientService.setPaymentRepository(paymentRepository);
    }

    @Test
    void testViewMyProjects() {
        Project project = new Project(1, "Project 1", "", null, 0, 0, null, null, 101);
        when(projectRepository.getProjectsByClientId(101))
                .thenReturn(Collections.singletonList(project));

        List<Project> result = clientService.viewMyProjects(101);

        assertEquals(1, result.size());
        assertEquals(project, result.get(0));
    }

    @Test
    void testViewMyPayments() {
        Payment payment = new Payment(1, 1000.0, null, null, null, 1, 101);
        when(paymentRepository.getPaymentsBy(any(Param.class)))
                .thenReturn(Collections.singletonList(payment));

        List<Payment> result = clientService.viewMyPayments(101);

        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
    }

    @Test
    void testGetTasks() {
        Task task = new Task(1, "Task 1", "Description", LocalDate.of(2025, 9, 14), LocalDate.of(2025, 9, 20), 0, 1);

        when(taskRepository.getTasksBy(any(Param.class)))
                .thenReturn(Collections.singletonList(task));

        List<Task> result = clientService.getTasks(1);

        assertEquals(1, result.size());
        assertEquals(task, result.get(0));
    }
}
