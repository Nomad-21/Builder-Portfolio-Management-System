package tech.zeta.repository;

import tech.zeta.model.Document;
import tech.zeta.util.DBUtil;
import tech.zeta.util.Param;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentRepository {

    // Create
    public void addDocument(Document doc) {
        String sql = "INSERT INTO Document (file_path, upload_date, project_id) VALUES (?, ?, ?)";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, doc.getFilePath());
            stmt.setTimestamp(2, Timestamp.valueOf(doc.getUploadDate()));
            stmt.setInt(3, doc.getProjectId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding document: " + e.getMessage());
        }
    }

    // Read by ID
    public <K,V> List<Document> getDocumentsBy(Param<K,V> param) {
        String sql = "SELECT * FROM Document WHERE "+param.getKey()+"=?";
        List<Document> documents = new ArrayList<>();
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, param.getValue());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                documents.add(new Document(
                        rs.getInt("document_id"),
                        rs.getString("file_path"),
                        rs.getTimestamp("upload_date").toLocalDateTime(),
                        rs.getInt("project_id")
                    )
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching document: " + e.getMessage());
        }
        return documents;
    }

    // Update
    public void updateDocument(Document doc) {
        String sql = "UPDATE Document SET file_path=?, upload_date=?, project_id=? WHERE document_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, doc.getFilePath());
            stmt.setTimestamp(2, Timestamp.valueOf(doc.getUploadDate()));
            stmt.setInt(3, doc.getProjectId());
            stmt.setInt(4, doc.getDocumentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating document: " + e.getMessage());
        }
    }

    // Delete
    public void deleteDocument(int documentId) {
        String sql = "DELETE FROM Document WHERE document_id=?";
        try {Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, documentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting document: " + e.getMessage());
        }
    }

    // Get all
    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM Document";
        try {Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                documents.add(new Document(
                        rs.getInt("document_id"),
                        rs.getString("file_path"),
                        rs.getTimestamp("upload_date").toLocalDateTime(),
                        rs.getInt("project_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching documents: " + e.getMessage());
        }
        return documents;
    }
}
