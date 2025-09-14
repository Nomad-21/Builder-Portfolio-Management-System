package tech.zeta.service;

import tech.zeta.model.Project;
import tech.zeta.model.Payment;
import tech.zeta.model.Task;
import tech.zeta.repository.PaymentRepository;
import tech.zeta.repository.ProjectRepository;
import tech.zeta.repository.TaskRepository;
import tech.zeta.util.Param;

import java.util.List;

public class ClientService {
     private TaskRepository taskRepository = new TaskRepository();
     private ProjectRepository projectRepository = new ProjectRepository();
     private PaymentRepository paymentRepository = new PaymentRepository();


    public void uploadPayment(Payment payment) {
        paymentRepository.addPayment(payment);
    }

    public List<Payment> viewMyPayments(int clientId) {
        Param<String, Integer> param = new Param<>("client_id", clientId);
        return paymentRepository.getPaymentsBy(param);
    }

    public List<Task> getTasks(int projectId) {
        Param<String, Integer> param = new Param<>("project_id", projectId);
        return taskRepository.getTasksBy(param);
    }


    public List<Project> viewMyProjects(int clientId) {
        return projectRepository.getProjectsByClientId(clientId);
    }

    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

}
