package io.ctrla.claims.services;


import io.ctrla.claims.entity.Roles;
import io.ctrla.claims.entity.User;
import io.ctrla.claims.entity.UserPrincipal;
import io.ctrla.claims.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserPrincipalService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserPrincipalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        // Check if user is null and throw an exception if not found
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Create UserPrincipal with user, userId, and role
        Long userId = user.getUserId(); // Assuming `User` has an `id` field
        String role = user.getRole().toString(); // Assuming `User` has a `role` field

        return new UserPrincipal(user, userId, role);
    }
}