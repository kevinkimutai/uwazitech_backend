package io.ctrla.claims.services;


import io.ctrla.claims.entity.PolicyHolder;
import io.ctrla.claims.entity.Roles;
import io.ctrla.claims.entity.User;
import io.ctrla.claims.entity.UserPrincipal;
import io.ctrla.claims.exceptions.NotFoundException;
import io.ctrla.claims.repo.PolicyHolderRepository;
import io.ctrla.claims.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserPrincipalService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PolicyHolderRepository policyHolderRepository;

    public UserPrincipalService(UserRepository userRepository, PolicyHolderRepository policyHolderRepository) {
        this.userRepository = userRepository;
        this.policyHolderRepository = policyHolderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Try to find the user by policy number
        User user = policyHolderRepository.findPolicyHolderByPolicyNumber(identifier)
                .map(PolicyHolder::getUser)
                .orElse(null);

        // If the user was not found by policy number, try finding them by email
        if (user == null) {
            user = userRepository.findByEmail(identifier);
        }

        // If the user is still not found, throw an exception
        if (user == null) {
            throw new UsernameNotFoundException("User not found with identifier: " + identifier);
        }

        // Build UserPrincipal object with user, userId, and role
        Long userId = user.getUserId(); // Assuming `User` has a `getUserId` method
        String role = user.getRole().toString(); // Assuming `User` has a `getRole` method

        return new UserPrincipal(user, userId, role);
    }
}
