package tech.zeta.controller;

import tech.zeta.model.User;
import tech.zeta.service.AuthService;

import java.util.Scanner;

public class AuthController {
    private final AuthService authService = new AuthService();

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String user = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        User loggedInUser = authService.login(user, pass);
        if (loggedInUser != null) {
            System.out.println("Login successful! Welcome " + loggedInUser.getRole());
        } else {
            System.out.println("Invalid credentials!");
        }
    }
}