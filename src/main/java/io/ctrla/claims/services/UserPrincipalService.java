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
        // User user = userRepository.findByEmail(email);

//        // Check if user is null and throw an exception if not found
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with email: " + email);
//        }


        User user = null;

        // Attempt to find by policy number
        PolicyHolder policyHolder = policyHolderRepository.findPolicyHolderByPolicyNumber(identifier)
                .orElseThrow(() -> new NotFoundException("No such PolicyHolder with that policy number"));
        if (policyHolder != null) {
            user = policyHolder.getUser();
        }

        // If not found by policy number, attempt to find by email
        if (user == null) {
            user = userRepository.findByEmail(identifier);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with identifier: " + identifier);
        }

        // Create UserPrincipal with user, userId, and role
        Long userId = user.getUserId(); // Assuming `User` has an `id` field
        String role = user.getRole().toString(); // Assuming `User` has a `role` field

        return new UserPrincipal(user, userId, role);
    }
}