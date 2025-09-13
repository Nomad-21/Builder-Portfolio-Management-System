package tech.zeta.service;


import tech.zeta.model.Project;
import tech.zeta.model.Task;
import tech.zeta.repository.ProjectRepository;
import tech.zeta.repository.TaskRepository;

import java.sql.SQLException;
import java.util.List;

public class BuilderService {
    ProjectRepository projectRepository = new ProjectRepository();
    TaskRepository taskRepository = new TaskRepository();

    public void editProject(int projectId, Object updatedProject) throws SQLException {
        // Implementation for editing a project
    }

    public int createProject(Project project) throws SQLException {
        return projectRepository.addProject(project);
    }

    public void createTask(Task task) throws SQLException {
        taskRepository.addTask(task);
    }

    public void uploadDocument(int projectId, String filePath) throws SQLException {
        // Implementation for uploading a document to a project
    }

    public void verifyPayments() throws SQLException {
        // Implementation for verifying payments
    }

    public void approvePayment(int paymentId) throws SQLException {
        // Implementation for approving a payment
    }

    public void rejectPayment(int paymentId) throws SQLException {
        // Implementation for rejecting a payment
    }

    public List<Object> viewMyProjects(int builderId) throws SQLException {
        // Implementation for viewing projects associated with the builder
        return null;
    }
}
