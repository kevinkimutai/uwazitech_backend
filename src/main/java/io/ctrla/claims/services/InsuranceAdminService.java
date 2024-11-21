package io.ctrla.claims.services;

import io.ctrla.claims.dto.hospital.HospitalAdminDto;
import io.ctrla.claims.dto.insuranceadmin.InsuranceAdminResponseDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.HospitalAdmin;
import io.ctrla.claims.entity.InsuranceAdmin;
import io.ctrla.claims.entity.User;
import io.ctrla.claims.exceptions.NotFoundException;
import io.ctrla.claims.mappers.InsuranceAdminMapper;
import io.ctrla.claims.repo.InsuranceAdminRepository;
import io.ctrla.claims.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class InsuranceAdminService {

    private final InsuranceAdminRepository insuranceAdminRepository;
    private final UserRepository userRepository;
    private final InsuranceAdminMapper insuranceAdminMapper;

    public InsuranceAdminService(InsuranceAdminRepository insuranceAdminRepository, UserRepository userRepository, InsuranceAdminMapper insuranceAdminMapper) {
        this.insuranceAdminRepository = insuranceAdminRepository;
        this.userRepository = userRepository;
        this.insuranceAdminMapper = insuranceAdminMapper;
    }


    public ApiResponse<InsuranceAdmin> getInsuranceAdminById (Long insuranceAdminId) {
        try {

            Optional<InsuranceAdmin> optionalInsuranceAdmin = insuranceAdminRepository.findById(insuranceAdminId);

            // If hospital is present, get it, otherwise throw an exception
            InsuranceAdmin insuranceAdmin = optionalInsuranceAdmin.orElseThrow(() -> new EntityNotFoundException("INsurance admin not found"));


            return new ApiResponse<>(200,"success",insuranceAdmin);
        }
        catch(NotFoundException e){
            return new ApiResponse<>(404, e.getMessage(), null);
        }
        catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching hospitalAdmin with Id", null);
        }

    }


    //Update InsuranceAdmin
    public ApiResponse<InsuranceAdminResponseDto> updateInsuranceAdmin(Long insuranceAdminId, HospitalAdminDto hospitalAdminDto) {
        try {

            InsuranceAdmin foundInsuranceAdmin = insuranceAdminRepository.findById(insuranceAdminId)
                    .orElseThrow(() -> new NotFoundException("No such InsuranceAdmin with that Id"));

            User user = userRepository.findById(foundInsuranceAdmin.getUser().getUserId())
                    .orElseThrow(() -> new NotFoundException("No such User with that Id"));;

            // Update only the fields that are provided in the DTO
            if (hospitalAdminDto.getIsVerified() != null) {
                user.setIsVerified(hospitalAdminDto.getIsVerified());
            }

            // Save the updated hospital to the database
            userRepository.save(user);

            InsuranceAdminResponseDto insuranceAdminResponseDto =  insuranceAdminMapper.toInsuranceAdminRes(foundInsuranceAdmin);

            return new ApiResponse<>(200, "success", insuranceAdminResponseDto);
        } catch (Exception e) {
            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while updating the hospital", null);
        }
    }
}
