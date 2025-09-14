package tech.zeta.controller;

import tech.zeta.model.*;
import tech.zeta.service.AuthService;
import tech.zeta.service.BuilderService;
import tech.zeta.util.GanttChartUtil;

import java.sql.SQLException;
import java.util.List;

public class BuilderController {

    BuilderService builderService = new BuilderService();
    private final User currentUser;

    public BuilderController(AuthService authService) {
        this.currentUser = authService.getLoggedInUser();
    }


    public void createProject(Project project, List<Task> taskList) {
        try {
            int projectId = builderService.createProject(project);
            for(Task task : taskList) {
                task.setProjectId(projectId);
                builderService.createTask(task);
            }
        } catch (SQLException exception) {
            System.out.println("Error creating project: " + exception.getMessage());
        }
    }

    public void editProject(Project updatedProject) {
        try {
            builderService.editProject(updatedProject);
            System.out.println("Project updated successfully.");
        } catch (SQLException exception) {
            System.out.println("Error editing project: " + exception.getMessage());
        }
    }

    public void uploadDocument(int projectId, String filePath) {
        try{
            Document document = new Document();
            document.setFilePath(filePath);
            document.setUploadDate(java.time.LocalDateTime.now());
            document.setProjectId(projectId);
            builderService.uploadDocument(document);
            System.out.println("Document uploaded successfully.");
        } catch (SQLException exception) {
            System.out.println("Error uploading document: " + exception.getMessage());
        }
    }

    public void showPendingPayments() {
        int builderId = currentUser.getUserId();
        List<Payment> projects = builderService.getPendingPayments(builderId);

        if (projects.isEmpty()) {
            System.out.println("No pending payments.");
        } else {
            projects.forEach(System.out::println);
        }
    }

    public void approvePayment(int paymentId) {
        try {
            builderService.approvePayment(paymentId);
            System.out.println("Payment approved successfully.");
        } catch (SQLException exception) {
            System.out.println("Error approving payment: " + exception.getMessage());
        }
    }

    public void rejectPayment(int paymentId) {
        try {
            builderService.rejectPayment(paymentId);
            System.out.println("Payment rejected successfully.");
        } catch (SQLException exception) {
            System.out.println("Error rejecting payment: " + exception.getMessage());
        }
    }

    public void viewMyProjects() {
        int clientId = currentUser.getUserId();
        List<Project> projects = builderService.viewMyProjects(clientId);

        if (projects.isEmpty()) {
            System.out.println("No projects assigned.");
        } else {
            projects.forEach(System.out::println);
        }
    }

    public void createTask(Task task) {
        try {
            builderService.createTask(task);
            System.out.println("Task created successfully.");
        } catch (SQLException exception) {
            System.out.println("Error creating task: " + exception.getMessage());
        }
    }

    public void editTask(Task updatedTask) {
        try{
            builderService.editTask(updatedTask);
            System.out.println("Task updated successfully.");
        } catch (SQLException exception) {
            System.out.println("Error editing task: " + exception.getMessage());
        }
    }

    public void removeTask(int taskId) {
        try {
            builderService.removeTask(taskId);
            System.out.println("Task removed successfully.");
        } catch (Exception e) {
            System.out.println("Error removing task: " + e.getMessage());
        }
    }

    public void viewGanttChart(int projectId) {
        try {
            List<Task> tasks = builderService.getTasks(projectId);
            GanttChartUtil.printGanttChart(tasks);
        } catch (Exception e) {
            System.out.println("Error displaying Gantt chart: " + e.getMessage());
        }
    }
}
