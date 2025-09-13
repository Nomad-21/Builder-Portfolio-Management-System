package tech.zeta.service;

import tech.zeta.model.User;
import tech.zeta.repository.UserRepository;
import tech.zeta.util.Param;

public class AuthService {
    private final UserRepository userRepository;
    private User loggedInUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String username, String password) {
        Param<String, String> param = new Param<>("name", username);
        User user = userRepository.getUserBy(param).getFirst();
        if (user != null && verifyPassword(password,user.getPassword())) {
            loggedInUser = user;
            return true;
        }
        return false;
    }

    private boolean verifyPassword(String password,String actualPassword){
        return password.equals(actualPassword);
    }

    public void logout() {
        loggedInUser = null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
