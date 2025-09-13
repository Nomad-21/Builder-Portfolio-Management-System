package tech.zeta.repository;

import tech.zeta.constants.Role;
import tech.zeta.model.User;
import tech.zeta.util.DBUtil;
import tech.zeta.util.Param;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    // Create
    public void addUser(User user) {
        String sql = "INSERT INTO \"User\" (name, email, password, role) VALUES (?, ?, ?, ?)";
        try{Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().name()); // assuming enum Role
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    // Read
    public <K,V> List<User> getUserBy(Param<K,V> param) {
        String sql = "SELECT * FROM \"User\" WHERE " + param.getKey() +" = ?";
        List<User> users = new ArrayList<>();

        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, param.getValue());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                  users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                    )
                  );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return users;
    }

    // Update
    public void updateUser(User user) {
        String sql = "UPDATE \"User\" SET name=?, email=?, password=?, role=? WHERE user_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().name());
            stmt.setInt(5, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    // Delete
    public void deleteUser(int userId) {
        String sql = "DELETE FROM \"User\" WHERE user_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    // Get all
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM \"User\"";
        try {Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }
}
