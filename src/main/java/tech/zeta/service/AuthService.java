package tech.zeta.service;

import tech.zeta.constants.Role;
import tech.zeta.model.User;
import tech.zeta.repository.UserRepository;
import tech.zeta.util.Param;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class AuthService {
    private final UserRepository userRepository;
    private User loggedInUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String username, String password) {
        Param<String, String> param = new Param<>("name", username);
        List<User> userList = userRepository.getUserBy(param);
        if(userList.isEmpty()){
            return false;
        }
        User user = userList.getFirst();
        if (user != null && verifyPassword(password,user.getPassword())) {
            loggedInUser = user;
            return true;
        }
        return false;
    }

    private boolean verifyPassword(String password,String actualPassword){
        String inputHashed = hashPassword(password);
        return inputHashed.equals(actualPassword);
    }



    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert to hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean registerUser(String name,String email, String password, Role role) {
        User newUser = new User(0,name, email, hashPassword(password), role);
        return userRepository.addUser(newUser);
    }

    public void logout() {
        loggedInUser = null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
