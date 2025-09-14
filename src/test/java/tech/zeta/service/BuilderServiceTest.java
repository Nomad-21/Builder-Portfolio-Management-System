package tech.zeta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.zeta.constants.PaymentStatus;
import tech.zeta.constants.ProjectStatus;
import tech.zeta.model.Payment;
import tech.zeta.model.Project;
import tech.zeta.repository.DocumentRepository;
import tech.zeta.repository.PaymentRepository;
import tech.zeta.repository.ProjectRepository;
import tech.zeta.repository.TaskRepository;
import tech.zeta.util.Param;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BuilderServiceTest {

    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private DocumentRepository documentRepository;
    private PaymentRepository paymentRepository;
    private BuilderService builderService;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        taskRepository = Mockito.mock(TaskRepository.class);
        documentRepository = Mockito.mock(DocumentRepository.class);
        paymentRepository = Mockito.mock(PaymentRepository.class);

        builderService = new BuilderService();
        // Inject mocks (overwrite the real repositories)
        builderService.projectRepository = projectRepository;
        builderService.taskRepository = taskRepository;
        builderService.documentRepository = documentRepository;
        builderService.paymentRepository = paymentRepository;
    }

    @Test
    void testCreateProject() throws SQLException {
        Project project = new Project(1, "House Build", "Build a house", ProjectStatus.UPCOMING, 5000, 0, null, null, 101);

        when(projectRepository.addProject(project)).thenReturn(1);

        int result = builderService.createProject(project);

        assertEquals(1, result);
        Mockito.verify(projectRepository).addProject(project);
    }

    @Test
    void testGetPendingPayments() {
        Project project1 = new Project(1, "Project 1", "", ProjectStatus.UPCOMING, 0, 0, null, null, 101);
        Project project2 = new Project(2, "Project 2", "", ProjectStatus.UPCOMING, 0, 0, null, null, 101);

        Payment payment1 = new Payment(10,1000.0, null, PaymentStatus.PENDING, null, 1, 201);

        Payment payment2 = new Payment(11, 2000.0, null, PaymentStatus.APPROVED, null, 1, 202);

        when(projectRepository.getProjectsBy(any())).thenReturn(Arrays.asList(project1, project2));

        when(paymentRepository.getPaymentsBy(new Param<>("project_id", 1))).thenReturn(Arrays.asList(payment1, payment2));


        List<Payment> result = builderService.getPendingPayments(101);

        assertEquals(2, result.size());
        assertTrue(result.contains(payment1));
    }

    @Test
    void testViewMyProjects() {
        Project project1 = new Project(1, "Project 1", "", ProjectStatus.UPCOMING, 0, 0, null, null, 101);

        when(projectRepository.getProjectsBy(any())).thenReturn(Collections.singletonList(project1));

        List<Project> result = builderService.viewMyProjects(101);

        assertEquals(1, result.size());
        assertEquals(project1, result.getFirst());
    }
}
