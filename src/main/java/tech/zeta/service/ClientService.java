package tech.zeta.service;

import tech.zeta.model.Project;
import tech.zeta.model.Payment;
import tech.zeta.repository.PaymentRepository;
import tech.zeta.repository.ProjectRepository;
import tech.zeta.repository.UserRepository;
import tech.zeta.util.Param;

import java.util.List;

public class ClientService {
    private final UserRepository userRepository = new UserRepository();
    private final ProjectRepository projectRepository = new ProjectRepository();
    private final PaymentRepository paymentRepository = new PaymentRepository();

    public List<Project> viewMyProjects(int clientId) {
        return projectRepository.getProjectsByClientId(clientId);
    }

    public void uploadPayment(Payment payment) {
        paymentRepository.addPayment(payment);
    }

    public List<Payment> viewMyPayments(int clientId) {
        Param<String, Integer> param = new Param<>("client_id", clientId);
        return paymentRepository.getPaymentBy(param);
    }
}
