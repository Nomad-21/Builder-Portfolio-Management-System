package tech.zeta.repository;

import tech.zeta.model.Task;
import tech.zeta.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    // Create
    public void addTask(Task task) {
        String sql = "INSERT INTO Task (name, description, start_date, end_date, progress, project_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, Date.valueOf(task.getStartDate()));
            stmt.setDate(4, Date.valueOf(task.getEndDate()));
            stmt.setInt(5, task.getProgress());
            stmt.setInt(6, task.getProjectId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding task: " + e.getMessage());
        }
    }

    // Read by ID
    public Task getTaskById(int taskId) {
        String sql = "SELECT * FROM Task WHERE task_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Task(
                        rs.getInt("task_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("progress"),
                        rs.getInt("project_id")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching task: " + e.getMessage());
        }
        return null;
    }

    // Update
    public void updateTask(Task task) {
        String sql = "UPDATE Task SET name=?, description=?, start_date=?, end_date=?, progress=?, project_id=? WHERE task_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, Date.valueOf(task.getStartDate()));
            stmt.setDate(4, Date.valueOf(task.getEndDate()));
            stmt.setInt(5, task.getProgress());
            stmt.setInt(6, task.getProjectId());
            stmt.setInt(7, task.getTaskId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }

    // Delete
    public void deleteTask(int taskId) {
        String sql = "DELETE FROM Task WHERE task_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }

    // Get all
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Task";
        try {Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("task_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("progress"),
                        rs.getInt("project_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tasks: " + e.getMessage());
        }
        return tasks;
    }
}
