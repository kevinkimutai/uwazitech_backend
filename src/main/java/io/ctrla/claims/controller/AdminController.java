package io.ctrla.claims.controller;


import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.services.AdminService;
import io.ctrla.claims.services.HospitalService;
import io.ctrla.claims.services.InsuranceService;
import io.ctrla.claims.services.PolicyHolderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final HospitalService hospitalService;
    private final InsuranceService insuranceService;
    private final PolicyHolderService policyHolderService;

    public AdminController(HospitalService hospitalService,
                           InsuranceService insuranceService, PolicyHolderService policyHolderService) {
        this.hospitalService = hospitalService;
        this.insuranceService = insuranceService;
        this.policyHolderService = policyHolderService;
    }

    //Create New Hospital
    @PostMapping(value = "/hospital")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<HospitalResponseDto>> createHospital(@Valid @RequestBody HospitalDto hospitalDto) {
        ApiResponse<HospitalResponseDto> apiResponse = hospitalService.createHospital(hospitalDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    //Get All Hospital Companies
    @GetMapping("/hospital")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<HospitalResponseDto>>> getHospitals() {
        ApiResponse<List<HospitalResponseDto>> apiResponse = hospitalService.getHospitals();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Get Hospital By ID
    @GetMapping("/hospital/{hospitalId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<HospitalResponseDto>> getHospitalById(@PathVariable Long hospitalId) {
        ApiResponse<HospitalResponseDto> apiResponse = hospitalService.getHospitalById(hospitalId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //Update Hospital
    @PatchMapping("/hospital/{hospitalId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<HospitalResponseDto>> updateHospital(
            @Valid @PathVariable Long hospitalId, @RequestBody HospitalDto hospitalDto) {
        ApiResponse<HospitalResponseDto> apiResponse = hospitalService.updateHospital( hospitalId, hospitalDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Delete Hospital
    @DeleteMapping("/hospital/{hospitalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHospital(@Valid @PathVariable Long hospitalId) {
        hospitalService.deleteInsurance(hospitalId);
    }

    //Insurances
    //Create Insurance Company
    @PostMapping(value = "/insurance", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<InsuranceResponseDto>> createInsuranceCompany(@Valid @RequestBody InsuranceDto insuranceDto) {
        ApiResponse<InsuranceResponseDto> apiResponse = insuranceService.createInsurance(insuranceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    //Get All Insurance Companies
    @GetMapping("/insurance")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<InsuranceResponseDto>>> getAllInsuranceCompanies() {
        ApiResponse<List<InsuranceResponseDto>> apiResponse = insuranceService.getAllInsurance();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Get Insurance By ID
    @GetMapping("/insurance/{insuranceId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<InsuranceResponseDto>> getInsuranceById(@PathVariable Long insuranceId) {
        ApiResponse<InsuranceResponseDto> apiResponse = insuranceService.getInsuranceById(insuranceId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //Update Insurance
    @PatchMapping("/insurance/{insuranceId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<InsuranceResponseDto>> updateOrder(@Valid @PathVariable Long insuranceId, @RequestBody InsuranceDto insuranceDto) {
        ApiResponse<InsuranceResponseDto> apiResponse = insuranceService.updateInsurance(insuranceId, insuranceDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Delete Insurance
    @DeleteMapping("/insurance/{insuranceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInsurance(@PathVariable Long insuranceId) {
        insuranceService.deleteInsurance(insuranceId);
    }


    ////PolicyHolders
    //Get All policyHolders
    @GetMapping("/policyholder")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<PolicyHolderDto>>> getPolicyHolders() {
        ApiResponse<List<PolicyHolderDto>> apiResponse = policyHolderService.getAllPolicyHolders();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Get PolicyHolder By ID
    @GetMapping("/policyholder/{policyHolderId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<PolicyHolderDto>> getPolicyHolderById(@PathVariable Long policyHolderId) {
        ApiResponse<PolicyHolderDto> apiResponse = policyHolderService.getPolicyHolderById(policyHolderId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //Users
    //Get All Users



}
