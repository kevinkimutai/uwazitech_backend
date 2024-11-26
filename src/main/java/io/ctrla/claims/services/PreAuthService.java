package io.ctrla.claims.services;


import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.preauth.PreAuthDto;
import io.ctrla.claims.dto.preauth.PreAuthResponseDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.Insurance;
import io.ctrla.claims.entity.PolicyHolder;
import io.ctrla.claims.entity.PreAuth;
import io.ctrla.claims.exceptions.NotFoundException;
import io.ctrla.claims.mappers.PreAuthMapper;
import io.ctrla.claims.repo.HospitalRepository;
import io.ctrla.claims.repo.InsuranceRepository;
import io.ctrla.claims.repo.PolicyHolderRepository;
import io.ctrla.claims.repo.PreAuthRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PreAuthService {

    private final PreAuthRepository preAuthRepository;
    private final HospitalRepository hospitalRepository;
    private final PreAuthMapper preAuthMapper;
    private final InsuranceRepository insuranceRepository;
    private final PolicyHolderRepository policyHolderRepository;

    public PreAuthService(PreAuthRepository preAuthRepository, HospitalRepository hospitalRepository,
                          PreAuthMapper preAuthMapper, InsuranceRepository insuranceRepository,
                          PolicyHolderRepository policyHolderRepository) {
        this.preAuthRepository = preAuthRepository;
        this.hospitalRepository = hospitalRepository;
        this.preAuthMapper = preAuthMapper;
        this.insuranceRepository = insuranceRepository;
        this.policyHolderRepository = policyHolderRepository;
    }


    //Create PreAuth
    public ApiResponse<PreAuthResponseDto> createPreAuth(PreAuthDto preAuthDto, Long hospitalId) {
        try{
            System.out.println("IDDD :"+hospitalId);

            //Get Hospital
            Optional<Hospital> optionalHospital = hospitalRepository.findById(hospitalId);
            // If hospital is present, get it, otherwise throw an exception
            Hospital hospital = optionalHospital.orElseThrow(() -> new EntityNotFoundException("Hospital not found"));

            //Get Insurance
            Optional<Insurance> optionalInsurance = insuranceRepository.findById(preAuthDto.getInsuranceId());
            Insurance insurance = optionalInsurance.orElseThrow(() -> new EntityNotFoundException("Insurance not found"));

            //Get policyHolder
            Optional<PolicyHolder> optionalPolicyHolder = policyHolderRepository.findPolicyHolderByPolicyNumber(preAuthDto.getPolicyNumber());
            PolicyHolder policyHolder = optionalPolicyHolder.orElseThrow(() -> new EntityNotFoundException("policy holder not found"));


            preAuthDto.setHospital(hospital);
            preAuthDto.setInsurance(insurance);
            preAuthDto.setPolicyHolder(policyHolder);

            System.out.println("PREAUTH :"+preAuthDto);

            PreAuth newPreAuth = preAuthRepository.save(preAuthMapper.toPreAuth(preAuthDto));
            PreAuthResponseDto preauthRes = preAuthMapper.toPreAuthRes(newPreAuth);

            return new ApiResponse<>(200,"success",preauthRes);
        }
        catch (Exception e) {
        log.error("e: ", e);
            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while creating preauth ", null);
        }
    }

    //Get All Preauths
    public ApiResponse<List<PreAuth>> getAllPreAuths() {
        try {
            List<PreAuth> preauths = preAuthRepository.findAll();

            if (preauths.isEmpty()) {
                throw new NotFoundException("No preauths found");
            }

            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    preauths);
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching all preauths", null);
        }
    }


    //Get All Preauths of Hospital
    public ApiResponse<List<PreAuthResponseDto>> getAllPreAuthsByHospital(Long hospitalId) {
        try {
            List<PreAuth> preauths = preAuthRepository.findPreAuthsByHospitalHospitalId(hospitalId);

            if (preauths.isEmpty()) {
                throw new NotFoundException("No preauths found");
            }

            List<PreAuthResponseDto> preauthsRes = preAuthMapper.toPreAuthsRes(preauths);

            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    preauthsRes);
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching all preauths", null);
        }
    }

    //Get All Preauths of Insurance
    public ApiResponse<List<PreAuth>> getAllPreAuthsByInsurance(Long insuranceId) {
        try {
            List<PreAuth> preauths = preAuthRepository.findPreAuthsByInsuranceInsuranceId(insuranceId);

            if (preauths.isEmpty()) {
                throw new NotFoundException("No preauths found");
            }

            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    preauths);
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching all preauths", null);
        }
    }

    //Get preauth by preauthId
    public ApiResponse<PreAuth> getPreAuthsById(Long preAuthId) {
        try {
            Optional<PreAuth> optionalPreAuth = preAuthRepository.findById(preAuthId);
            PreAuth preAuth = optionalPreAuth.orElseThrow(() -> new EntityNotFoundException("preauth not found"));

            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    preAuth);
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching preauth with id", null);
        }
    }

    //Update Preauth
    public ApiResponse<PreAuth> updatePreAuth(Long preAuthId, PreAuthDto preAuthDto) {
        try {
            PreAuth foundPreAuth = preAuthRepository.findById(preAuthId)
                    .orElseThrow(() -> new NotFoundException("No such preauth with that Id"));

            // Update only the fields that are provided in the DTO
            if (preAuthDto.getIsApproved() != null) {
                foundPreAuth.setIsApproved(preAuthDto.getIsApproved() );
            }


            // Save the updated entity
            PreAuth savedPreAuth = preAuthRepository.save(foundPreAuth);

            return new ApiResponse<>(200, "success", savedPreAuth);
        } catch (Exception e) {
            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while updating the preauth", null);
        }
    }
}
