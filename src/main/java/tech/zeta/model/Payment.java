package tech.zeta.model;

import tech.zeta.constants.PaymentStatus;

import java.time.LocalDateTime;

public class Payment {

    private int paymentId;
    private double amount;
    private LocalDateTime date;
    private PaymentStatus status;
    private String proofPath;
    private int projectId;
    private int clientId;

    // Constructors
    public Payment() {}

    public Payment(int paymentId,Double amount, LocalDateTime date, PaymentStatus status,
                   String proofPath, int projectId, int clientId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.proofPath = proofPath;
        this.projectId = projectId;
        this.clientId = clientId;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getProofPath() {
        return proofPath;
    }

    public void setProofPath(String proofPath) {
        this.proofPath = proofPath;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }


    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", proofPath='" + proofPath + '\'' +
                ", projectId=" + projectId +
                ", clientId=" + clientId +
                '}';
    }
}
