package tech.zeta.model;

import java.time.LocalDateTime;

public class Document {
    private int documentId;
    private String filePath;
    private LocalDateTime uploadDate;
    private int projectId;

    // Default constructor
    public Document() {}

    // Parameterized constructor
    public Document(int documentId, String filePath, LocalDateTime uploadDate, int projectId) {
        this.documentId = documentId;
        this.filePath = filePath;
        this.uploadDate = uploadDate;
        this.projectId = projectId;
    }

    // Getters and Setters
    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId=" + documentId +
                ", filePath='" + filePath + '\'' +
                ", uploadDate=" + uploadDate +
                ", projectId=" + projectId +
                '}';
    }
}
