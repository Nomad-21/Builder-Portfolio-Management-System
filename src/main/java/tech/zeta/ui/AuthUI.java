package tech.zeta.ui;

import java.util.Scanner;

public class AuthUI {
    private static Scanner scanner = new Scanner(System.in);
    public void showMenu() {
        System.out.println("1. Login");
        System.out.println("2. Logout");
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
}
