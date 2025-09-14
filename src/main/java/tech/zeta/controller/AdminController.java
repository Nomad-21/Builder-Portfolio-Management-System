package tech.zeta.controller;

import tech.zeta.constants.PaymentStatus;
import tech.zeta.constants.Role;
import tech.zeta.model.Payment;
import tech.zeta.model.Project;
import tech.zeta.model.Task;
import tech.zeta.model.User;
import tech.zeta.service.AdminService;


import java.sql.SQLException;
import java.util.List;

public class AdminController {
    AdminService adminService = new AdminService();

    public void deleteUser(int userId) {
        adminService.deleteUser(userId);
    }

    public List<Project> getAllProjects() {
        return adminService.getAllProjects();
    }


    public void showPendingPayments() {
        List<Payment> pendingPayments = adminService.getPendingPayments();
        if (pendingPayments.isEmpty()) {
            System.out.println("No pending payments found.");
        } else {
            System.out.println("\n--- Pending Payments ---");
            pendingPayments.forEach(System.out::println);
        }
    }

    public void updatePaymentStatus(int paymentId, PaymentStatus status) {
        try {
            adminService.updatePaymentStatus(paymentId, status);
            System.out.println("Payment status updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating payment status: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    public void createUser(String name, String email, String password, Role role) {
        User newUser = new User(0,name, email, password, role);
        adminService.createUser(newUser);
    }

    public void createProject(Project project, List<Task> taskList) {
        try {
            int projectId = adminService.createProject(project);
            for (Task task : taskList) {
                task.setProjectId(projectId);
                adminService.createTask(task);
            }
            System.out.println("Project created successfully.");
        }catch (SQLException e){
            System.out.println("Error creating project: " + e.getMessage());
        }
    }

    public void deleteProject(int projectId) {
        adminService.deleteProject(projectId);
    }

    public void associateProjectWithUsers(int projectId, int builderId, int clientId) {
         adminService.updateProject(projectId, builderId, clientId);
    }
}
