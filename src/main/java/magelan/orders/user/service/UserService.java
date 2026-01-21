package magelan.orders.user.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import magelan.orders.exception.UserNotFoundException;
import magelan.orders.security.UserData;
import magelan.orders.user.model.User;
import magelan.orders.user.model.UserRole;
import magelan.orders.user.repository.UserRepository;
import magelan.orders.web.dto.ChangePasswordRequest;
import magelan.orders.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void register(RegisterRequest registerRequest) {
        log.info("Registering new user with username {}", registerRequest.getUsername());

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .active(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        log.info("User {} registered successfully with ID {}",
                registerRequest.getUsername(), user.getId());
    }

    @Cacheable("users")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found.", id);
                    return new UserNotFoundException(id);
                });
    }

    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {

        User user = getById(userId);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is not correct.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user details for authentication: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Authentication failed. Username {} not found.", username);
                    return new UsernameNotFoundException(
                            "Username [%s] is not found.".formatted(username)
                    );
                });

        log.info("User {} authenticated successfully (ID: {}, role: {}, active: {}).",
                username, user.getId(), user.getRole(), user.isActive());

        return new UserData(user.getId(), username, user.getPassword(), user.getRole(), user.isActive());
    }
}
