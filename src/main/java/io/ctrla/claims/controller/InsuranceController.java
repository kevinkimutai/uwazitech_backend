package io.ctrla.claims.controller;



import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.services.InsuranceService;
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


    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    //Get All Insurance Companies
    @GetMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<InsuranceResponseDto>>> getAllInsuranceCompanies() {
        ApiResponse<List<InsuranceResponseDto>> apiResponse = insuranceService.getAllInsurance();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Upload Invoice Claim
//    @PostMapping("/{insuranceId}/invoice")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<ApiResponse<InsuranceResponseDto>> uploadInvoice(@Valid @PathVariable Integer insuranceId, @RequestBody InsuranceDto insuranceDto) {
//        ApiResponse<InsuranceResponseDto> apiResponse = insuranceService.updateInsurance(insuranceId,insuranceDto);
//
//        return ResponseEntity.ok(apiResponse);
//    }

}
