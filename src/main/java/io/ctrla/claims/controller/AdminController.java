package io.ctrla.claims.controller;


import io.ctrla.claims.dto.hospital.HospitalAdminDto;
import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderRes;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.HospitalAdmin;
import io.ctrla.claims.entity.InsuranceAdmin;
import io.ctrla.claims.entity.PolicyHolder;
import io.ctrla.claims.entity.PreAuth;
import io.ctrla.claims.services.*;
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
    private final PreAuthService preAuthService;
    private final HospitalAdminService hospitalAdminService;
    private final InsuranceAdminService insuranceAdminService;

    public AdminController(HospitalService hospitalService,
                           InsuranceService insuranceService,
                           PolicyHolderService policyHolderService,
                           PreAuthService preAuthService,
                           HospitalAdminService hospitalAdminService,
                           InsuranceAdminService insuranceAdminService) {
        this.hospitalService = hospitalService;
        this.insuranceService = insuranceService;
        this.policyHolderService = policyHolderService;
        this.preAuthService = preAuthService;
        this.hospitalAdminService = hospitalAdminService;
        this.insuranceAdminService = insuranceAdminService;

    }

    //Create New Hospital
    @PostMapping(value = "/hospital")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<HospitalResponseDto>> createHospital(@Valid @RequestBody HospitalDto hospitalDto) {
        ApiResponse<HospitalResponseDto> apiResponse = hospitalService.createHospital(hospitalDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

//    //Get Hospital stats
//    @PostMapping(value = "/hospital/stats")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<ApiResponse<List<HospitalResponseDto>>> getHospitalStats() {
//        ApiResponse<List<HospitalResponseDto>> apiResponse = hospitalService.getHospitalsStats();
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
//    }

    //Get All Hospital Companies
    @GetMapping("/hospital")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<HospitalResponseDto>>> getHospitals() {
        ApiResponse<List<HospitalResponseDto>> apiResponse = hospitalService.getHospitals();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Get Hospital By ID
    @GetMapping("/hospital/{hospitalId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<HospitalResponseDto>> getHospitalById(@PathVariable Long hospitalId) {
        ApiResponse<HospitalResponseDto> apiResponse = hospitalService.getHospitalById(hospitalId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //Update Hospital
    @PatchMapping("/hospital/{hospitalId}")
    @ResponseStatus(HttpStatus.OK)
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



//    //Get PolicyHolder By ID
//    @GetMapping("/policyholder/{policyHolderId}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<ApiResponse<PolicyHolderRes>> getPolicyHolderById(@PathVariable Long policyHolderId) {
//        ApiResponse<PolicyHolderRes> apiResponse = policyHolderService.getPolicyHolderById(policyHolderId);
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
//    }


    //Users
    //Get All HospitalAdmins
    @GetMapping("/hospitaladmins")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<HospitalAdmin>>> getHospitalAdmins() {
        ApiResponse<List<HospitalAdmin>> apiResponse = hospitalService.getHospitalAdmins();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Get HospitalAdmin By Id
    @GetMapping("/hospitaladmins/{hospitalAdminId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<HospitalAdmin>> getHospitalAdminById(@PathVariable Long hospitalAdminId) {
        ApiResponse<HospitalAdmin> apiResponse = hospitalAdminService.getHospitalAdminById(hospitalAdminId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Update HospitalAdmin
    @PatchMapping("/hospitaladmins/{hospitalAdminId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<HospitalAdmin>> updateHospitalAdmin(@Valid @PathVariable Long hospitalAdminId, @RequestBody HospitalAdminDto hospitalAdminDto) {
        ApiResponse<HospitalAdmin> apiResponse = hospitalAdminService.updateHospitalAdmin(hospitalAdminId, hospitalAdminDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }



    //Get All InsuranceAdmins
    @GetMapping("/insuranceadmins")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<InsuranceAdmin>>> getInsuranceAdmins() {
        ApiResponse<List<InsuranceAdmin>> apiResponse = insuranceService.getInsuranceAdmins();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Update InsuranceAdmin
    @PatchMapping("/insuranceadmins/{insuranceAdminId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<InsuranceAdmin>> updateInsuranceAdmin(@Valid @PathVariable Long insuranceAdminId, @RequestBody HospitalAdminDto hospitalAdminDto) {
        ApiResponse<InsuranceAdmin> apiResponse = insuranceAdminService.updateInsuranceAdmin(insuranceAdminId, hospitalAdminDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //Get InsuranceAdmin By Id
    @GetMapping("/insuranceadmins/{insuranceAdminId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<InsuranceAdmin>> getInsuranceAdminById(@PathVariable Long insuranceAdminId) {
        ApiResponse<InsuranceAdmin> apiResponse = insuranceAdminService.getInsuranceAdminById(insuranceAdminId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //Get All PolicyHolders
    @GetMapping("/policyHolders")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<PolicyHolder>>> getPolicyHolderAdmins() {
        ApiResponse<List<PolicyHolder>> apiResponse = policyHolderService.getAllPolicyHolders();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //PreAuths
    //Get All PreAuths
    @GetMapping("/preauth")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<PreAuth>>> getPreAuths() {
        ApiResponse<List<PreAuth>> apiResponse = preAuthService.getAllPreAuths();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Get PreAuth By Id
    @GetMapping("/preauth/{preAuthId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<PreAuth>> getPreAuthById(@PathVariable Long preAuthId) {
        ApiResponse<PreAuth> apiResponse = preAuthService.getPreAuthsById(preAuthId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }



}
