package tech.zeta.ui;

import tech.zeta.constants.PaymentStatus;
import tech.zeta.constants.ProjectStatus;
import tech.zeta.constants.Role;
import tech.zeta.controller.AdminController;
import tech.zeta.model.Project;
import tech.zeta.model.Task;
import tech.zeta.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminUI {

    private final AdminController adminController;
    private final Scanner scanner;

    public AdminUI() {
        this.adminController = new AdminController();
        this.scanner = new Scanner(System.in);
    }

    public  void start() {
        while (true) {
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. View All Users");
            System.out.println("2. Create User");
            System.out.println("3. Delete User");
            System.out.println("4. View All Projects");
            System.out.println("5. Create Project");
            System.out.println("6. Delete Project");
            System.out.println("7. Verify/Reject Payments");
            System.out.println("8. Associate Builders with Projects");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> viewAllUsers();
                case 2 -> createUser();
                case 3 -> deleteUser();
                case 4 -> viewAllProjects();
                case 5 -> createProjectFlow();
                case 6 -> deletProject();
                case 7 -> verifyPayments();
                case 8 -> associateProject();
                case 9 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option! Try again.");
            }
        }
    }

    private void viewAllUsers() {
        List<User> users = adminController.getAllUsers();
        System.out.println("\n--- Users ---");
        users.forEach(System.out::println);
    }

    private void createUser() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String role;
        while(true) {
            System.out.print("Enter role (ADMIN/BUILDER/CLIENT): ");
            role = scanner.nextLine();
            if(role.equals("ADMIN") || role.equals("BUILDER") || role.equals("CLIENT")){
                break;
            }
            System.out.println("Invalid role! Please enter ADMIN, BUILDER, or CLIENT.");
        }

        adminController.createUser(name, email, password, Role.valueOf(role));
        System.out.println("User created successfully.");
    }

    private void deleteUser() {
        System.out.print("Enter user ID to delete: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        adminController.deleteUser(userId);
        System.out.println("User deleted.");
    }

    private void viewAllProjects() {
        List<Project> projects = adminController.getAllProjects();
        System.out.println("\n--- Projects ---");
        projects.forEach(System.out::println);
    }


    private void verifyPayments() {
        adminController.showPendingPayments();
        System.out.print("Enter payment ID to verify/reject: ");
        int paymentId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter status (Approved/Rejected): ");
        String status = scanner.nextLine();

        adminController.updatePaymentStatus(paymentId, PaymentStatus.valueOf(status.toUpperCase()));
        System.out.println("Payment status updated.");
    }

    private void createProjectFlow() {
        try {
            // 1. Collect Project Details
            System.out.print("Project Name: ");
            String name = scanner.nextLine();
            System.out.print("Description: ");
            String desc = scanner.nextLine();
            System.out.print("Budget: ");
            double budget = scanner.nextDouble();
            scanner.nextLine(); // consume newline
            System.out.print("Start Date (YYYY-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());
            System.out.print("End Date (YYYY-MM-DD): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Builder ID : ");
            int builderId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            Project project = new Project();
            project.setName(name);
            project.setDescription(desc);
            project.setBudget(budget);
            project.setStartDate(startDate);
            project.setEndDate(endDate);
            project.setStatus(ProjectStatus.UPCOMING);
            project.setBuilderId(builderId);

            // 2. Collect Task Details
            System.out.print("How many tasks does this project have? ");
            int numTasks = scanner.nextInt();
            scanner.nextLine(); // consume newline

            List<Task> taskList = new ArrayList<>();
            for (int i = 1; i <= numTasks; i++) {
                System.out.println("Enter details for Task #" + i + ":");
                System.out.print("Task Name: ");
                String taskName = scanner.nextLine();
                System.out.print("Description: ");
                String taskDesc = scanner.nextLine();
                System.out.print("Start Date (YYYY-MM-DD): ");
                LocalDate taskStart = LocalDate.parse(scanner.nextLine());
                System.out.print("End Date (YYYY-MM-DD): ");
                LocalDate taskEnd = LocalDate.parse(scanner.nextLine());

                scanner.nextLine(); // consume newline

                Task task = new Task();
                task.setName(taskName);
                task.setDescription(taskDesc);
                task.setStartDate(taskStart);
                task.setEndDate(taskEnd);
                task.setProjectId(project.getProjectId());

                taskList.add(task);
            }

            adminController.createProject(project,taskList);
            System.out.println("All tasks added successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private void deletProject() {
        System.out.print("Enter project ID to delete: ");
        int projectId = scanner.nextInt();
        scanner.nextLine();
        adminController.deleteProject(projectId);
        System.out.println("Project deleted.");
    }

    private void associateProject() {
        System.out.print("Enter project ID: ");
        int projectId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter builder ID to associate: ");
        int builderId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter client ID to associate: ");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        adminController.associateProjectWithUsers(projectId, builderId, clientId);
    }
}
