package tech.zeta.model;

import tech.zeta.constants.ProjectStatus;
import tech.zeta.repository.ProjectRepository;

import java.time.LocalDate;

public class Project {
    private int projectId;
    private String name;
    private String description;
    private ProjectStatus status;
    private double budget;
    private double actualSpend;
    private LocalDate startDate;
    private LocalDate endDate;
    private int builderId;

    public Project(){};

    public Project(int projectId, String name, String description, ProjectStatus status,
                   double budget, double actualSpend, LocalDate startDate, LocalDate endDate,int builderId) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.budget = budget;
        this.actualSpend = actualSpend;
        this.startDate = startDate;
        this.endDate = endDate;
        this.builderId = builderId;
    }

    public int getProjectId() { return projectId; }
    public String getName() { return name; }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getActualSpend() {
        return actualSpend;
    }

    public void setActualSpend(double actualSpend) {
        this.actualSpend = actualSpend;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getBuilderId() {
        return builderId;
    }

    public void setBuilderId(int builderId) {
        this.builderId = builderId;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + projectId +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", budget=" + budget +
                ", actualSpend=" + actualSpend +
                '}';
    }


}
