package tech.zeta.controller;

import tech.zeta.constants.PaymentStatus;
import tech.zeta.model.Project;
import tech.zeta.model.Payment;
import tech.zeta.model.Task;
import tech.zeta.model.User;
import tech.zeta.service.AuthService;
import tech.zeta.service.ClientService;

import java.time.LocalDateTime;
import java.util.List;

public class ClientController {

    private final ClientService clientService = new ClientService();
    private final User currentUser;

    public ClientController(AuthService authService) {
        this.currentUser = authService.getLoggedInUser();
    }

    public void showProjects() {
        int clientId = currentUser.getUserId();
        List<Project> projects = clientService.viewMyProjects(clientId);
        if (projects.isEmpty()) {
            System.out.println("No projects assigned.");
        } else {
            for(Project project : projects) {
                System.out.println(project.toString());
                System.out.println("Tasks:");
                List<Task> tasks = clientService.getTasks(project.getProjectId());
                tasks.forEach(task -> System.out.println(task.toString()));
                System.out.println("-----");
            }
        }
    }

    public void showPayments() {
        int clientId = currentUser.getUserId();
        List<Payment> payments = clientService.viewMyPayments(clientId);
        if (payments.isEmpty()) {
            System.out.println("No payments recorded.");
        } else {
            payments.forEach(payment -> System.out.println(payment.toString()));
        }
    }

    public void makePayment(int projectId, double amount, String proofPath) {
        //need to configure managerId later
        Payment payment = new Payment(0,amount, LocalDateTime.now(), PaymentStatus.PENDING,proofPath, projectId, currentUser.getUserId());
        clientService.uploadPayment(payment);
        System.out.println("Payment uploaded successfully!");
    }
}
