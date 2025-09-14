package tech.zeta.service;


import tech.zeta.constants.PaymentStatus;
import tech.zeta.constants.ProjectStatus;
import tech.zeta.model.Document;
import tech.zeta.model.Payment;
import tech.zeta.model.Project;
import tech.zeta.model.Task;
import tech.zeta.repository.DocumentRepository;
import tech.zeta.repository.PaymentRepository;
import tech.zeta.repository.ProjectRepository;
import tech.zeta.repository.TaskRepository;
import tech.zeta.util.ModelFiller;
import tech.zeta.util.Param;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuilderService {
    ProjectRepository projectRepository = new ProjectRepository();
    TaskRepository taskRepository = new TaskRepository();
    DocumentRepository documentRepository = new DocumentRepository();
    PaymentRepository paymentRepository = new PaymentRepository();

    public void editProject(Project updatedProject) throws SQLException {
        Project currentProject = projectRepository.getProjectsBy(new Param<>("project_id", updatedProject.getProjectId())).getFirst();
        updatedProject = ModelFiller.fillProject(currentProject, updatedProject);
        projectRepository.updateProject(updatedProject);
    }

    public int createProject(Project project) throws SQLException {
        return projectRepository.addProject(project);
    }

    public void createTask(Task task) throws SQLException {
        taskRepository.addTask(task);
        checkProjectStatus(task.getProjectId(), task);
    }

    public void uploadDocument(Document document) throws SQLException {
        documentRepository.addDocument(document);
    }


    public List<Payment> getPendingPayments(int builder_id) {

        List<Integer> projectIds = projectRepository.getProjectsBy(new Param<>("builder_id", builder_id))
                                                    .stream().map(Project::getProjectId).toList();
        List<Payment> pendingPayments = new ArrayList<>();
        for(int projectId : projectIds){
            List<Payment> payments = paymentRepository.getPaymentsBy(new Param<>("project_id", projectId));
            for(Payment payment : payments){
                if(payment.getStatus() == null || payment.getStatus()== PaymentStatus.PENDING){
                    pendingPayments.add(payment);
                }
            }
        }

        return pendingPayments;
    }

    public void approvePayment(int paymentId) throws SQLException {
        Payment payment = paymentRepository.getPaymentsBy(new Param<>("payment_id", paymentId)).getFirst();
        payment.setStatus(PaymentStatus.APPROVED);
        paymentRepository.updatePayment(payment);
        updateProjectAmount(payment.getProjectId(), payment.getAmount());
    }

    public void updateProjectAmount(int projectId, double amount) throws SQLException {
        Project project = projectRepository.getProjectsBy(new Param<>("project_id", projectId)).getFirst();
        double updatedAmountPaid = project.getActualSpend() + amount;
        project.setActualSpend(updatedAmountPaid);
        projectRepository.updateProject(project);
    }

    public void rejectPayment(int paymentId) throws SQLException {
        Payment payment = paymentRepository.getPaymentsBy(new Param<>("payment_id", paymentId)).getFirst();
        payment.setStatus(PaymentStatus.REJECTED);
        paymentRepository.updatePayment(payment);
    }

    public void removeTask(int taskId) throws SQLException {
        taskRepository.deleteTask(taskId);
    }

    public List<Project> viewMyProjects(int builderId) {
        Param<String, Integer> param = new Param<>("builder_id", builderId);
        List<Project> projects = projectRepository.getProjectsBy(param);
        return projects;
    }

    public void editTask(Task task) throws SQLException {
        Task currentTask = taskRepository.getTasksBy(new Param<>("task_id", task.getTaskId())).getFirst();
        task = ModelFiller.fillTask(currentTask, task);
        checkProjectStatus(task.getProjectId(), task);
        taskRepository.updateTask(task);
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

    public List<Task> getTasks(int projectId) {
        Param<String, Integer> param = new Param<>("project_id", projectId);
        return taskRepository.getTasksBy(param);
    }
}
