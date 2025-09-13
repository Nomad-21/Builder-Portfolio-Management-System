package tech.zeta.controller;

import tech.zeta.model.Project;
import tech.zeta.model.Task;
import tech.zeta.service.AuthService;
import tech.zeta.service.BuilderService;
import tech.zeta.ui.BuilderUI;

import java.sql.SQLException;
import java.util.List;

public class BuilderController {

    BuilderService builderService = new BuilderService();

    public BuilderController(AuthService authService) {
    }
    public void editProject(int projectId, Project updatedProject) {
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

    public void uploadDocument(int projectId, String filePath) {
    }

    public void verifyPayments() {
    }

    public void approvePayment(int paymentId) {
    }

    public void rejectPayment(int paymentId) {
    }

    public void viewMyProjects() {
    }

    public void createTask(Task task) {
    }
}
