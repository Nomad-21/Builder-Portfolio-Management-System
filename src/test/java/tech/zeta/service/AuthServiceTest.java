package tech.zeta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.zeta.constants.Role;
import tech.zeta.model.User;
import tech.zeta.repository.UserRepository;
import tech.zeta.util.Param;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AuthServiceTest {

    private UserRepository userRepository;
    private AuthService authService;

    static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        authService = new AuthService(userRepository);
    }

    @Test
    void testLoginSuccess() {
        User user = new User(1, "Alice", "alice@test.com", hash("password123"), Role.CLIENT);

        Mockito.when(userRepository.getUserBy(any()))
                .thenReturn(List.of(user));

        boolean result = authService.login("Alice", "password123");

        assertTrue(result);
        assertEquals(user, authService.getLoggedInUser());
    }

    @Test
    void testLoginFailure_WrongPassword() {
        User user = new User(1, "Bob", "bob@test.com", hash("password123"), Role.CLIENT);

        Mockito.when(userRepository.getUserBy(any()))
                .thenReturn(List.of(user));

        boolean result = authService.login("Bob", "wrongPassword");

        assertFalse(result);
        assertNull(authService.getLoggedInUser());
    }

    @Test
    void testLoginFailure_UserNotFound() {
        Mockito.when(userRepository.getUserBy(any()))
                .thenReturn(Collections.emptyList());

        boolean result = authService.login("Charlie", "whatever");

        assertFalse(result);
        assertNull(authService.getLoggedInUser());
    }

    @Test
    void testRegisterUser() {
        Mockito.when(userRepository.addUser(any())).thenReturn(true);

        boolean result = authService.registerUser("Dave", "dave@test.com", "mypassword", Role.ADMIN);

        assertTrue(result);
        Mockito.verify(userRepository).addUser(any(User.class));
    }
}
