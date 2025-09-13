package tech.zeta.controller;

import tech.zeta.model.User;
import tech.zeta.service.AuthService;
import tech.zeta.ui.AuthUI;

import java.util.Scanner;

public class AuthController {
    private AuthService authService;
    private AuthUI authUI;
   // private DashboardController dashboardController;

    public AuthController(AuthService authService, AuthUI authUI) {
        this.authService = authService;
        this.authUI = authUI;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            authUI.showMenu();
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    String username = authUI.promptUsername();
                    String password = authUI.promptPassword();
                    if (authService.login(username, password)) {
                        authUI.showMessage("Login successful!");
                        DashboardController.showDashboard(authService);
                    } else {
                        authUI.showMessage("Invalid credentials!");
                    }
                }
                case 2 -> {
                    authService.logout();
                    authUI.showMessage("Logged out successfully!");
                }
                case 3 -> {
                    authUI.showMessage("Exiting...");
                    return;
                }
                default -> authUI.showMessage("Invalid choice!");
            }
        }
    }
}
