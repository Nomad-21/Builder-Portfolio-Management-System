package tech.zeta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.zeta.constants.PaymentStatus;
import tech.zeta.model.Payment;
import tech.zeta.model.Project;
import tech.zeta.model.User;
import tech.zeta.repository.PaymentRepository;
import tech.zeta.repository.ProjectRepository;
import tech.zeta.repository.TaskRepository;
import tech.zeta.repository.UserRepository;
import tech.zeta.util.Param;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;

class AdminServiceTest {

    private AdminService adminService;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        // mock dependencies
        userRepository = mock(UserRepository.class);
        projectRepository = mock(ProjectRepository.class);
        taskRepository = mock(TaskRepository.class);
        paymentRepository = mock(PaymentRepository.class);

        // inject mocks into AdminService
        adminService = new AdminService();
        adminService.userRepository = userRepository;
        adminService.projectRepository = projectRepository;
        adminService.taskRepository = taskRepository;
        adminService.paymentRepository = paymentRepository;
    }

    @Test
    void testCreateUser() {
        User user = new User();
        adminService.createUser(user);

        verify(userRepository, times(1)).addUser(user);
    }

    @Test
    void testDeleteUser() {
        adminService.deleteUser(5);

        verify(userRepository, times(1)).deleteUser(5);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.getAllUsers()).thenReturn(List.of(new User(), new User()));

        List<User> result = adminService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).getAllUsers();
    }

    @Test
    void testGetAllProjects() {
        when(projectRepository.getAllProjects()).thenReturn(List.of(new Project()));

        List<Project> projects = adminService.getAllProjects();

        assertEquals(1, projects.size());
        verify(projectRepository, times(1)).getAllProjects();
    }

    @Test
    void testGetPendingPayments() {
        when(paymentRepository.getPaymentsBy(any())).thenReturn(List.of(new Payment()));

        List<Payment> payments = adminService.getPendingPayments();

        assertEquals(1, payments.size());
        verify(paymentRepository, times(1)).getPaymentsBy(any());
    }

    @Test
    void testUpdatePaymentStatus() {
        Payment payment = new Payment();
        payment.setPaymentId(10);
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.getPaymentsBy(any())).thenReturn(List.of(payment));

        adminService.updatePaymentStatus(10, PaymentStatus.APPROVED);

        assertEquals(PaymentStatus.APPROVED, payment.getStatus());
        verify(paymentRepository, times(1)).updatePayment(payment);
    }
}
