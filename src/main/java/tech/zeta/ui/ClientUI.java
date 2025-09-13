package tech.zeta.ui;

import tech.zeta.constants.PaymentStatus;
import tech.zeta.controller.ClientController;
import tech.zeta.model.Payment;
import tech.zeta.service.AuthService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ClientUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static ClientController clientController;

    public ClientUI(AuthService authService) {
        clientController = new ClientController(authService);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Client Dashboard ===");
            System.out.println("1. View My Projects");
            System.out.println("2. View My Payments");
            System.out.println("3. Upload Payment Proof");
            System.out.println("0. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> clientController.showProjects();
                case 2 -> clientController.showPayments();
                case 3 -> uploadPayment();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void uploadPayment() {
        System.out.print("Enter Project ID: ");
        int projectId = scanner.nextInt();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Proof File Path: ");
        String proofPath = scanner.nextLine();

        clientController.makePayment( projectId,amount, proofPath);
    }
}
