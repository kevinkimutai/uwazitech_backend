package io.ctrla.claims.services;


import io.ctrla.claims.dto.AuthDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.dto.user.UserDto;
import io.ctrla.claims.entity.*;
import io.ctrla.claims.mappers.InsuranceMapper;
import io.ctrla.claims.mappers.UserMapper;
import io.ctrla.claims.repo.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final HospitalAdminRepository hospitalAdminRepository;
    private final HospitalRepository hospitalRepository;
    private final InsuranceRepository insuranceRepository;
    private final InsuranceAdminRepository insuranceAdminRepository;

    private final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder(12);
    private final AdminRepository adminRepository;
    private final PolicyHolderService policyHolderService;
    private final PolicyHolderRepository policyHolderRepository;

    public AuthService(
            UserRepository userRepository,
            UserMapper userMapper,
            AuthenticationManager authManager,
            JWTService jwtService,
            HospitalAdminRepository hospitalAdminRepository,
            HospitalRepository hospitalRepository,
            InsuranceRepository insuranceRepository,
            InsuranceAdminRepository insuranceAdminRepository,
            AdminRepository adminRepository, PolicyHolderService policyHolderService, PolicyHolderRepository policyHolderRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.insuranceAdminRepository = insuranceAdminRepository;
        this.hospitalAdminRepository = hospitalAdminRepository;
        this.hospitalRepository = hospitalRepository;
        this.insuranceRepository = insuranceRepository;
        this.adminRepository = adminRepository;
        this.policyHolderService = policyHolderService;
        this.policyHolderRepository = policyHolderRepository;
    }

    public ApiResponse<UserDto> registerNewUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        UserDto savedUserDto = new UserDto();
        savedUserDto.setFirstName(savedUser.getFirstName());
        savedUserDto.setLastName(savedUser.getLastName());
        savedUserDto.setEmail(savedUser.getEmail());
        savedUserDto.setSignUpType(userDto.getSignUpType());

        String policyNumber = userDto.getPolicyNumber();
        String signUpType = userDto.getSignUpType();

        if ("hospital".equals(signUpType)) {
            // Logic for hospital admin
            HospitalAdmin hospitalAdmin = new HospitalAdmin();
            hospitalAdmin.setUser(savedUser);

            Hospital hospital = hospitalRepository.findById(userDto.getHospitalId())
                    .orElseThrow(() -> new IllegalArgumentException("Hospital not found with id: " + userDto.getHospitalId()));

            hospitalAdmin.setHospital(hospital);
            savedUser.setRole(Roles.ROLE_HOSPITAL_ADMIN);

            userRepository.save(savedUser);
            hospitalAdminRepository.save(hospitalAdmin);

        } else if ("insurance".equals(signUpType)) {
            // Logic for insurance admin
            InsuranceAdmin insuranceAdmin = new InsuranceAdmin();
            insuranceAdmin.setUser(savedUser);

            Insurance insurance = insuranceRepository.findById(userDto.getInsuranceId())
                    .orElseThrow(() -> new IllegalArgumentException("Insurance not found with id: " + userDto.getInsuranceId()));

            insuranceAdmin.setInsurance(insurance);
            savedUser.setRole(Roles.ROLE_INSURANCE_ADMIN);

            userRepository.save(savedUser);
            insuranceAdminRepository.save(insuranceAdmin);

        }  else if (signUpType == null && policyNumber != null) {
            savedUser.setRole(Roles.ROLE_POLICYHOLDER);
            savedUser.setIsVerified(true);

            PolicyHolder pHolder = new PolicyHolder();
            pHolder.setUser(savedUser); // Set the saved user directly
            pHolder.setPolicyNumber(policyNumber);

            Insurance insurance = insuranceRepository.findById(userDto.getInsuranceId())
                    .orElseThrow(() -> new IllegalArgumentException("Insurance not found with id: " + userDto.getInsuranceId()));

            pHolder.setInsurance(insurance);


            userRepository.save(savedUser);
            policyHolderRepository.save(pHolder);
        }else if (signUpType == null) {
            // Default role as ADMIN when signUpType is null
            savedUser.setRole(Roles.ROLE_ADMIN);

            AdminEntity admin = new AdminEntity();
            admin.setUser(savedUser); // Set the saved user directly

            // No need to fetch the user again
            adminRepository.save(admin);
        } else {
            throw new IllegalArgumentException("Invalid sign-up type: " + signUpType);
        }

        // Prepare response
        return new ApiResponse<>(201, "success", savedUserDto);
    }


    public ApiResponse<AuthDto> login(UserDto user) {
        String identifier;
        User foundUser;

        // Determine if the user is logging in with a policy number or email
        if (user.getPolicyNumber() != null && !user.getPolicyNumber().isEmpty()) {
            System.out.println("1");
            identifier = user.getPolicyNumber();
            Optional<PolicyHolder> optionalPolicyHolder = policyHolderRepository.findPolicyHolderByPolicyNumber(user.getPolicyNumber());
            PolicyHolder policyHolder = optionalPolicyHolder.orElseThrow(() -> new EntityNotFoundException("policyholder not found"));

            foundUser = policyHolder.getUser();
        } else {
            System.out.println("2");
            identifier = user.getEmail();
            foundUser = userRepository.findByEmail(identifier);

        }

//        // Authenticate the user
//        Authentication authentication = authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(identifier, user.getPassword())
//        );

        Authentication authentication;

        try {
             authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identifier, user.getPassword())
            );

            System.out.println("Authentication successful: " + authentication.isAuthenticated());
        } catch (Exception e) {
            e.printStackTrace();

            return new ApiResponse<>(401, "Invalid credentials", null);
        }


        if (authentication.isAuthenticated() && foundUser != null) {
            System.out.println("3");
            String generatedToken = jwtService.generateToken(foundUser);
            AuthDto authDto = new AuthDto();
            authDto.setToken(generatedToken);
            return new ApiResponse<>(200, "success", authDto);
        } else {
            System.out.println("4");
            return new ApiResponse<>(401, "Invalid credentials", null);
        }
    }



    public Long getCurrentUserId() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUserId();
    }

    public String getCurrentUserRole() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getRole();
    }
}


