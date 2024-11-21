package io.ctrla.claims.services;


import io.ctrla.claims.dto.hospital.HospitalAdminDto;
import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.hospitaladmin.HospitalAdminResponseDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.HospitalAdmin;
import io.ctrla.claims.entity.User;
import io.ctrla.claims.exceptions.NotFoundException;
import io.ctrla.claims.mappers.HospitalAdminMapper;
import io.ctrla.claims.repo.HospitalAdminRepository;
import io.ctrla.claims.repo.HospitalRepository;
import io.ctrla.claims.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HospitalAdminService {

    private final HospitalAdminRepository hospitalAdminRepository;
    private final UserRepository userRepository;
    private final HospitalAdminMapper hospitalAdminMapper;

    public HospitalAdminService(HospitalAdminRepository hospitalRepository,HospitalAdminMapper hospitalAdminMapper,UserRepository userRepository) {
        this.hospitalAdminRepository = hospitalRepository;
        this.userRepository = userRepository;
        this.hospitalAdminMapper = hospitalAdminMapper;
    }

    public ApiResponse<HospitalAdmin> getHospitalAdminById (Long hospitalAdminId){
        try {

            Optional<HospitalAdmin> optionalHospital = hospitalAdminRepository.findById(hospitalAdminId);

            // If hospital is present, get it, otherwise throw an exception
            HospitalAdmin hospitalAdmin = optionalHospital.orElseThrow(() -> new EntityNotFoundException("Hospital not found"));


            return new ApiResponse<>(200,"success",hospitalAdmin);
        }
        catch(NotFoundException e){
            return new ApiResponse<>(404, e.getMessage(), null);
        }
        catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching hospitalAdmin with Id", null);
        }

    }

    public ApiResponse<HospitalAdminResponseDto> updateHospitalAdmin(Long hospitalAdminId, HospitalAdminDto hospitalAdminDto) {
        try {

            HospitalAdmin foundHospitalAdmin = hospitalAdminRepository.findById(hospitalAdminId)
                    .orElseThrow(() -> new NotFoundException("No such hospitalAdmin with that Id"));

            User user = userRepository.findById(foundHospitalAdmin.getUser().getUserId())
                    .orElseThrow(() -> new NotFoundException("No such User with that Id"));;

            // Update only the fields that are provided in the DTO
            if (hospitalAdminDto.getIsVerified() != null) {
                user.setIsVerified(hospitalAdminDto.getIsVerified());
            }

            // Save the updated hospital to the database
            userRepository.save(user);

            HospitalAdminResponseDto hospitalAdminResponseDto =  hospitalAdminMapper.toHospitalAdminRes(foundHospitalAdmin);

                      return new ApiResponse<>(200, "success", hospitalAdminResponseDto);
        } catch (Exception e) {
            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while updating the hospital", null);
        }
    }
}
