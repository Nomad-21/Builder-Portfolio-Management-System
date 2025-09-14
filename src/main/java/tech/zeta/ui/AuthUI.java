package tech.zeta.ui;


import tech.zeta.controller.AuthController;
import java.util.Scanner;

public class AuthUI {
    private static Scanner scanner = new Scanner(System.in);
    public void showMenu() {
        System.out.println("1. Login");
        System.out.println("2. Signup");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
    }


    public String promptUsername() {
        System.out.print("Enter username: ");
        return scanner.nextLine();
    }

    public String promptPassword() {
        System.out.print("Enter password: ");
        return scanner.nextLine();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showSignupForm(AuthController authController) {
        System.out.println("===== User Signup =====");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Select role (BUILDER / CLIENT): ");
        String role = scanner.nextLine().toUpperCase();

        if (authController.registerUser(name, email, password, role)) {
            System.out.println("Signup successful! You can now log in.");
        } else {
            System.out.println("Signup failed. Email might already be registered.");
        }
    }
}
