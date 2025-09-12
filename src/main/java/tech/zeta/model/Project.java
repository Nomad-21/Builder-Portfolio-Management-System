package tech.zeta.model;

public class Project {
    private int id;
    private String name;
    private String status; // Upcoming, In Progress, Completed
    private double budget;
    private double spent;

    public Project(int id, String name, String status, double budget, double spent) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.budget = budget;
        this.spent = spent;
    }

    public String getName() { return name; }
    public String getStatus() { return status; }
}