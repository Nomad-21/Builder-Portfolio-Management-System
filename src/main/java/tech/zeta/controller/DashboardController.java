package tech.zeta.controller;

import tech.zeta.model.User;
import tech.zeta.service.AuthService;
import tech.zeta.ui.AdminUI;
import tech.zeta.ui.BuilderUI;
import tech.zeta.ui.ClientUI;

public class DashboardController {
    public static void showDashboard(AuthService authService) {
        System.out.println("Welcome to the Dashboard!");

        User currentUser = authService.getLoggedInUser();

        switch (currentUser.getRole()) {
            case ADMIN -> {
                AdminUI adminUI = new AdminUI();
                adminUI.start();
            }
            case CLIENT -> {
                ClientUI clientUI = new ClientUI(authService);
                clientUI.showMenu();
            }
           case BUILDER -> {
                BuilderUI builderUI = new BuilderUI(authService);
                builderUI.showMenu();
            }
            default -> System.out.println("Unknown role!");
        }
    }
}
