package io.ctrla.claims.services;


import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.hospitaladmin.HospitalAdminResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.HospitalAdmin;
import io.ctrla.claims.entity.Insurance;
import io.ctrla.claims.exceptions.NotFoundException;
import io.ctrla.claims.mappers.HospitalAdminMapper;
import io.ctrla.claims.mappers.HospitalMapper;
import io.ctrla.claims.repo.HospitalAdminRepository;
import io.ctrla.claims.repo.HospitalRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;
    private final JWTService jwtService;
    private final HospitalAdminRepository hospitalAdminRepository;
    private final HospitalAdminMapper hospitalAdminMapper;

    public HospitalService(
            HospitalRepository hospitalRepository,HospitalAdminMapper hospitalAdminMapper, HospitalMapper hospitalMapper,JWTService jwtService, HospitalAdminRepository hospitalAdminRepository) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalMapper = hospitalMapper;
        this.jwtService = jwtService;
        this.hospitalAdminRepository = hospitalAdminRepository;
        this.hospitalAdminMapper = hospitalAdminMapper;
    }


    //Get All Registered Hospital Companies
    public ApiResponse<List<HospitalResponseDto>> getHospitals() {
        try {
            List<HospitalResponseDto> hospitals = hospitalMapper.toHospitalDto(hospitalRepository.findAll());

            if (hospitals.isEmpty()) {
                throw new NotFoundException("No hospital companies found");
            }

            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    hospitals);
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching hospital companies", null);
        }
    }


//    //Get Hospital Stats
//    public ApiResponse<List<HospitalResponseDto>> getHospitalsStats() {
//        try {
//            Long hospitalCount = hospitalRepository.count();
//
//            if (hospitals.isEmpty()) {
//                throw new NotFoundException("No hospital companies found");
//            }
//
//            // Return success response
//            return new ApiResponse<>(
//                    200,
//                    "success",
//                    hospitals);
//        } catch (NotFoundException nfe) {
//            // Handle not found scenario
//            return new ApiResponse<>(404, nfe.getMessage(), null);
//
//        } catch (Exception e) {
//
//            // Return error response for unexpected errors
//            return new ApiResponse<>(500, "An error occurred while fetching hospital companies", null);
//        }
//    }


    //Get HospitalCompany By Id
    public ApiResponse<HospitalResponseDto> getHospitalById (Long hospitalId){
        try {

            Optional<Hospital> optionalHospital = hospitalRepository.findById(hospitalId);

            // If hospital is present, get it, otherwise throw an exception
            Hospital hospital = optionalHospital.orElseThrow(() -> new EntityNotFoundException("Hospital not found"));

            // Map the hospital to the required result
            HospitalResponseDto result = hospitalMapper.toHospitalRes(hospital);

            return new ApiResponse<>(200,"success",result);
        }
        catch(NotFoundException e){
            return new ApiResponse<>(404, e.getMessage(), null);
        }
        catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching insurance with Id", null);
        }

    }

    //Create Hospital Company
    public ApiResponse<HospitalResponseDto> createHospital(HospitalDto hospitalDto){
        try{
            Hospital newHospital = hospitalRepository.save(hospitalMapper.toHospital(hospitalDto));
            HospitalResponseDto result = hospitalMapper.toHospitalRes(newHospital);

            return new ApiResponse<>(200,"success",result);
        }
        catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while creating hospital", null);
        }
    }

    // Update Hospital
    public ApiResponse<HospitalResponseDto> updateHospital(Long hospitalId, HospitalDto hospitalDto) {
        try {

            Hospital foundHospital = hospitalRepository.findById(hospitalId)
                    .orElseThrow(() -> new NotFoundException("No such hospital with that Id"));

            // Update only the fields that are provided in the DTO
            if (hospitalDto.getHospitalName() != null) {
                foundHospital.setHospitalName(hospitalDto.getHospitalName());
            }
            if (hospitalDto.getHospitalAddress() != null) {
                foundHospital.setHospitalAddress(hospitalDto.getHospitalAddress());
            }
            if (hospitalDto.getHospitalBranch() != null) {
                foundHospital.setHospitalBranch(hospitalDto.getHospitalBranch());
            }

            // Save the updated hospital to the database
            HospitalResponseDto result = hospitalMapper.toHospitalRes(hospitalRepository.save(foundHospital));

            return new ApiResponse<>(200, "success", result);
        } catch (Exception e) {
            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while updating the hospital", null);
        }
    }
    //Delete Hospital
    public void deleteInsurance(Long hospitalId) {
        try {
            hospitalRepository.deleteById(hospitalId);
        } catch (EmptyResultDataAccessException e) {
            // Handle case where insurance with the given ID does not exist
            throw new EntityNotFoundException("Insurance with ID " + hospitalId + " not found.");
        } catch (DataIntegrityViolationException e) {
            // Handle cases where deleting the insurance violates database integrity (e.g., foreign key constraints)
            throw new RuntimeException("Cannot delete insurance with ID " + hospitalId + " due to data integrity violation.");
        } catch (Exception e) {
            // Log and rethrow unexpected exceptions for further handling
            throw new RuntimeException("An unexpected error occurred while trying to delete insurance with ID " + hospitalId, e);
        }
    }

    //Get Hospital Admins
    public ApiResponse<List<HospitalAdminResponseDto>> getHospitalAdmins() {
        try {
            List<Hospital> hospitals = hospitalRepository.findAll();
            if (hospitals.isEmpty()) {
                throw new NotFoundException("No hospital companies found");
            }


            List<HospitalAdmin> hospitalAdmins = hospitals.stream()
                    .flatMap(hospital -> hospital.getHospitalAdmins().stream())
                    .collect(Collectors.toList());

           List<HospitalAdminResponseDto> hospitalAdminsRes = hospitalAdminMapper.toHospitalAdminDto(hospitalAdmins);

            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    hospitalAdminsRes);
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching hospital admins", null);
        }
    }

}
