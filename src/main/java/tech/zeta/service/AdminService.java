package tech.zeta.service;

import tech.zeta.constants.PaymentStatus;
import tech.zeta.constants.ProjectStatus;
import tech.zeta.model.*;
import tech.zeta.repository.*;
import tech.zeta.util.Param;

import java.sql.SQLException;
import java.util.List;

public class AdminService {
    UserRepository userRepository = new UserRepository();
    ProjectRepository projectRepository = new ProjectRepository();
    TaskRepository taskRepository = new TaskRepository();
    PaymentRepository paymentRepository = new PaymentRepository();
    DocumentRepository documentRepository = new DocumentRepository();

    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }

    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    public List<Payment> getPendingPayments() {
        return paymentRepository.getPaymentsBy(new Param<>("status", PaymentStatus.PENDING.name()));
    }

    public void updatePaymentStatus(int paymentId, PaymentStatus status) throws SQLException {
        Payment payment = paymentRepository.getPaymentsBy(new Param<>("payment_id", paymentId)).getFirst();
        payment.setStatus(status);
        if(status== PaymentStatus.APPROVED){
           updateProjectAmount(payment.getProjectId(), payment.getAmount());
        }
        paymentRepository.updatePayment(payment);
    }

    public void updateProjectAmount(int projectId, double amount) throws SQLException {
        Project project = projectRepository.getProjectsBy(new Param<>("project_id", projectId)).getFirst();
        double updatedAmountPaid = project.getActualSpend() + amount;
        project.setActualSpend(updatedAmountPaid);
        projectRepository.updateProject(project);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void createUser(User newUser) {
        userRepository.addUser(newUser);
    }

    public int createProject(Project project) throws SQLException {
        return projectRepository.addProject(project);
    }

    public void createTask(Task task) throws SQLException {
        taskRepository.addTask(task);
        checkProjectStatus(task.getProjectId(), task);
    }

    private void updateProjectStatus(int projectId, ProjectStatus status) throws SQLException {
        Project project = projectRepository.getProjectsBy(new Param<>("project_id", projectId)).getFirst();
        project.setStatus(status);
        projectRepository.updateProject(project);
    }

    private void checkProjectStatus(int projectId,Task task){
        try {
            if(task.getProgress() == 100){
                checkForProjectCompletion(task.getProjectId());
            }else if(task.getProgress() > 0){
                Project project = projectRepository.getProjectsBy(new Param<>("project_id", task.getProjectId())).getFirst();
                if(project.getStatus() == ProjectStatus.UPCOMING){
                    updateProjectStatus(project.getProjectId(), ProjectStatus.IN_PROGRESS);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkForProjectCompletion(int projectId) {
        List<Task> tasks = taskRepository.getTasksBy(new Param<>("project_id", projectId));
        boolean allCompleted = tasks.stream().allMatch(task -> task.getProgress() == 100);
        if (allCompleted) {
            Project project = projectRepository.getProjectsBy(new Param<>("project_id", projectId)).getFirst();
            project.setStatus(ProjectStatus.COMPLETED);
            projectRepository.updateProject(project);
            System.out.println("Hurray!! All tasks completed. Project marked as COMPLETED.");
        }
    }

    public void updateProject(int projectId, int builderId, int clientId) {
        Project project = projectRepository.getProjectsBy(new Param<>("project_id", projectId)).getFirst();
        if (project == null) {
            System.out.println("Project with ID " + projectId + " does not exist.");
            return;
        }
        project.setBuilderId(builderId);
        projectRepository.updateProject(project);
        projectRepository.addProjectClient(projectId,clientId);
        System.out.println("Project associated with builder and client successfully.");
    }

    public void deleteProject(int projectId) {
        Project project = projectRepository.getProjectsBy(new Param<>("project_id", projectId)).getFirst();
        if (project == null) {
            System.out.println("Project with ID " + projectId + " does not exist.");
            return;
        }
        Param<String, Integer> param = new Param<>("project_id", projectId);
        List<Task> tasks = taskRepository.getTasksBy(param);
        for (Task task : tasks) taskRepository.deleteTask(task.getTaskId());

        List<Payment>  payments = paymentRepository.getPaymentsBy(param);
        for(Payment payment : payments) paymentRepository.deletePayment(payment.getPaymentId());

        List<Document> documents = documentRepository.getDocumentsBy(param);
        for(Document document : documents) documentRepository.deleteDocument(document.getDocumentId());

        projectRepository.deleteProject(projectId);
    }
}
