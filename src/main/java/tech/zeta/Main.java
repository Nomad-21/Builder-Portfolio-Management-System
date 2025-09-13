package tech.zeta;

import tech.zeta.controller.AuthController;
import tech.zeta.repository.UserRepository;
import tech.zeta.service.AuthService;
import tech.zeta.ui.AuthUI;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        AuthService authService = new AuthService(userRepository);
        AuthUI authUI = new AuthUI();
        AuthController authController = new AuthController(authService, authUI);
        authController.start();
    }
}
