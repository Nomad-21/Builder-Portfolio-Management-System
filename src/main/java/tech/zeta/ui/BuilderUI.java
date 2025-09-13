package tech.zeta.ui;

import tech.zeta.constants.ProjectStatus;
import tech.zeta.controller.BuilderController;
import tech.zeta.model.Project;
import tech.zeta.model.Task;
import tech.zeta.model.User;
import tech.zeta.service.AuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class BuilderUI {
    private final BuilderController builderController;
    private final  Scanner scanner = new Scanner(System.in);

    private final  User currentBuilder;

    public BuilderUI(AuthService authService) {
        this.currentBuilder = authService.getLoggedInUser();
        this.builderController = new BuilderController(authService);
    }

    public  void showMenu() {
        while (true) {
            System.out.println("\n--- Builder Menu ---");
            System.out.println("1. View My Projects");
            System.out.println("2. Update My Projects");
            System.out.println("3. Create Project");
            System.out.println("4. Upload Document");
            System.out.println("5. Verify Payments");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> builderController.viewMyProjects();
                case 2 -> editProjectFlow();
                case 3 -> createProjectFlow();
                case 4 -> uploadDocumentFlow();
                case 5 -> verifyPaymentsFlow();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
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

            Project project = new Project();
            project.setName(name);
            project.setDescription(desc);
            project.setBudget(budget);
            project.setStartDate(startDate);
            project.setEndDate(endDate);
            project.setStatus(ProjectStatus.UPCOMING);
            project.setBuilderId(currentBuilder.getUserId());

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

            builderController.createProject(project,taskList);
            System.out.println("All tasks added successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            scanner.nextLine(); // clear buffer in case of error
        }
    }


    private void editProjectFlow() {
        System.out.print("Enter Project ID to edit: ");
        int projectId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter new details (leave blank to keep current value):");

        System.out.print("Project Name: ");
        String name = scanner.nextLine();

        System.out.print("Description: ");
        String desc = scanner.nextLine();

        System.out.print("Budget: ");
        String budgetInput = scanner.nextLine();

        System.out.print("Start Date (YYYY-MM-DD): ");
        String startDateInput = scanner.nextLine();

        System.out.print("End Date (YYYY-MM-DD): ");
        String endDateInput = scanner.nextLine();

        System.out.print("Status (Upcoming/In Progress/Completed): ");
        String status = scanner.nextLine();

        Project updatedProject = new Project();
        if (!name.isBlank()) updatedProject.setName(name);
        if (!desc.isBlank()) updatedProject.setDescription(desc);
        if (!budgetInput.isBlank()) updatedProject.setBudget(Double.parseDouble(budgetInput));
        if (!startDateInput.isBlank()) updatedProject.setStartDate(LocalDate.parse(startDateInput));
        if (!endDateInput.isBlank()) updatedProject.setEndDate(LocalDate.parse(endDateInput));
        if (!status.isBlank()) updatedProject.setStatus(ProjectStatus.valueOf(status));

        builderController.editProject(projectId, updatedProject);
    }


    private void uploadDocumentFlow() {
        System.out.print("Project ID: ");
        int projectId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Document file path: ");
        String filePath = scanner.nextLine();
        builderController.uploadDocument(projectId, filePath);
    }

    private void verifyPaymentsFlow() {
        builderController.verifyPayments();
        System.out.print("Enter Payment ID to Approve/Reject (0 to skip): ");
        int paymentId = scanner.nextInt();
        scanner.nextLine();
        if (paymentId != 0) {
            System.out.print("Approve or Reject (A/R): ");
            String action = scanner.nextLine();
            if (action.equalsIgnoreCase("A")) builderController.approvePayment(paymentId);
            else if (action.equalsIgnoreCase("R")) builderController.rejectPayment(paymentId);
        }
    }
}
