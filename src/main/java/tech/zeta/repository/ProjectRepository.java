package tech.zeta.repository;

import tech.zeta.model.Project;
import tech.zeta.constants.ProjectStatus;
import tech.zeta.util.DBUtil;
import tech.zeta.util.Param;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {

    // Create
    public int addProject(Project project) {
        String sql = "INSERT INTO Project (name, description, status, budget, actual_spend, start_date, end_date, builder_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int affectedRows = 0;
        PreparedStatement statement = null;
        try {Connection connection = DBUtil.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, project.getName());
            statement.setString(2, project.getDescription());
            statement.setString(3, project.getStatus().name());
            statement.setDouble(4, project.getBudget());
            statement.setDouble(5, project.getActualSpend());
            statement.setDate(6, Date.valueOf(project.getStartDate()));
            statement.setDate(7, Date.valueOf(project.getEndDate()));
            statement.setInt(8, project.getBuilderId());
            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding project: " + e.getMessage());
        }

        if(affectedRows>0){
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }catch (SQLException e) {
                System.out.println("Error fetching generated project ID: " + e.getMessage());
            }
        }

        return -1;
    }

    // Read
    public <K,V> List<Project> getProjectsBy(Param<K,V> params) {
        String sql = "SELECT * FROM Project WHERE " + params.getKey() + "=?";
        List<Project> projects = new ArrayList<>();
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             statement.setObject(1, params.getValue());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                projects.add(new Project(
                        rs.getInt("project_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        ProjectStatus.valueOf(rs.getString("status")),
                        rs.getDouble("budget"),
                        rs.getDouble("actual_spend"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("builder_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching projects: " + e.getMessage());
        }
        return projects;
    }

    // Update
    public void updateProject(Project project) {
        String sql = "UPDATE Project SET name=?, description=?, status=?, budget=?, actual_spend=?, start_date=?, end_date=?, builder_id=? WHERE project_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, project.getName());
            statement.setString(2, project.getDescription());
            statement.setString(3, project.getStatus().name());
            statement.setDouble(4, project.getBudget());
            statement.setDouble(5, project.getActualSpend());
            statement.setDate(6, Date.valueOf(project.getStartDate()));
            statement.setDate(7, Date.valueOf(project.getEndDate()));
            statement.setInt(8, project.getBuilderId());
            statement.setInt(9, project.getProjectId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating project: " + e.getMessage());
        }
    }

    // Delete
    public void deleteProject(int projectId) {
        String sql = "DELETE FROM Project WHERE project_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, projectId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting project: " + e.getMessage());
        }
    }

    // Get all
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM Project";
        try {Connection conn = DBUtil.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                projects.add(new Project(
                        rs.getInt("project_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        ProjectStatus.valueOf(rs.getString("status")),
                        rs.getDouble("budget"),
                        rs.getDouble("actual_spend"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("builder_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching projects: " + e.getMessage());
        }
        return projects;
    }

    public List<Project> getProjectsByClientId(int clientId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM Project where project_id IN (SELECT project_id FROM Client_Project WHERE client_id = ?)";
        try {Connection conn = DBUtil.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, clientId);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                projects.add(new Project(
                        rs.getInt("project_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        ProjectStatus.valueOf(rs.getString("status")),
                        rs.getDouble("budget"),
                        rs.getDouble("actual_spend"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("builder_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching projects: " + e.getMessage());
        }
        return projects;
    }

    public void addProjectClient(int projectId, int clientId) {
        String sql = "INSERT INTO Client_Project (client_id, project_id) VALUES (?, ?)";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, clientId);
            statement.setInt(2, projectId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error associating project with client: " + e.getMessage());
        }
    }
}
