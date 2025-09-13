package tech.zeta.repository;

import tech.zeta.model.Payment;
import tech.zeta.constants.PaymentStatus;
import tech.zeta.util.DBUtil;
import tech.zeta.util.Param;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    // Create
    public void addPayment(Payment payment) {
        String sql = "INSERT INTO Payment (amount, date, status, proof_path, project_id, client_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, payment.getAmount());
            stmt.setTimestamp(2, Timestamp.valueOf(payment.getDate()));
            stmt.setString(3, payment.getStatus().name());
            stmt.setString(4, payment.getProofPath());
            stmt.setInt(5, payment.getProjectId());
            stmt.setInt(6, payment.getClientId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding payment: " + e.getMessage());
        }
    }

    // Read by ID
    public <K,V> List<Payment> getPaymentBy(Param<K,V> param) {
        String sql = "SELECT * FROM Payment WHERE " + param.getKey() + "=?";
        List<Payment> payments = new ArrayList<>();
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, param.getValue());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                payments.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        PaymentStatus.valueOf(rs.getString("status")),
                        rs.getString("proof_path"),
                        rs.getInt("project_id"),
                        rs.getInt("client_id")
                    )
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching payment: " + e.getMessage());
        }
        return payments;
    }

    // Update
    public void updatePayment(Payment payment) {
        String sql = "UPDATE Payment SET amount=?, date=?, status=?, proof_path=?, project_id=?, client_id=? WHERE payment_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, payment.getAmount());
            stmt.setTimestamp(2, Timestamp.valueOf(payment.getDate()));
            stmt.setString(3, payment.getStatus().name());
            stmt.setString(4, payment.getProofPath());
            stmt.setInt(5, payment.getProjectId());
            stmt.setInt(6, payment.getClientId());
            stmt.setInt(7, payment.getPaymentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating payment: " + e.getMessage());
        }
    }

    // Delete
    public void deletePayment(int paymentId) {
        String sql = "DELETE FROM Payment WHERE payment_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, paymentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting payment: " + e.getMessage());
        }
    }

    // Get all
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment";
        try {Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        PaymentStatus.valueOf(rs.getString("status")),
                        rs.getString("proof_path"),
                        rs.getInt("project_id"),
                        rs.getInt("client_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching payments: " + e.getMessage());
        }
        return payments;
    }
}
