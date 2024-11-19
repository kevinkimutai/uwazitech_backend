package io.ctrla.claims.controller;



import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.policyholder.CheckPwdPolicyHolder;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.services.InsuranceService;
import io.ctrla.claims.services.PolicyHolderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.List;

@RestController
@RequestMapping("/api/v1/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final PolicyHolderService policyHolderService;


    public InsuranceController(InsuranceService insuranceService,PolicyHolderService policyHolderService) {
        this.insuranceService = insuranceService;
        this.policyHolderService = policyHolderService;
    }

    //Get All Insurance Companies
    @GetMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<InsuranceResponseDto>>> getAllInsuranceCompanies() {
        ApiResponse<List<InsuranceResponseDto>> apiResponse = insuranceService.getAllInsurance();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

   //Check If User has Pwd
   @GetMapping( "/policyholder/{policyNumber}")
   @ResponseStatus(HttpStatus.OK)
   public ResponseEntity<ApiResponse<CheckPwdPolicyHolder>> getPolicyHolderPwdStatus(@PathVariable String policyNumber) {
       ApiResponse<CheckPwdPolicyHolder> apiResponse = policyHolderService.checkPwdPolicyHolderByPolicyNumber(policyNumber);
       return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
   }


}
