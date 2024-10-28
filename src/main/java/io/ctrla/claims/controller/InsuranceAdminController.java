package io.ctrla.claims.controller;

import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.services.InsuranceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/insuranceadmin")
public class InsuranceAdminController {
    private final InsuranceService insuranceService;


    public InsuranceAdminController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }


    //Get Insurance By ID
    @GetMapping("/{insuranceId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<InsuranceResponseDto>> getInsuranceById(@PathVariable Long insuranceId) {
        ApiResponse<InsuranceResponseDto> apiResponse = insuranceService.getInsuranceById(insuranceId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //Update Insurance
    @PatchMapping("/{insuranceId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<InsuranceResponseDto>> updateOrder(@Valid @PathVariable Long insuranceId, @RequestBody InsuranceDto insuranceDto) {
        ApiResponse<InsuranceResponseDto> apiResponse = insuranceService.updateInsurance(insuranceId, insuranceDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

}
