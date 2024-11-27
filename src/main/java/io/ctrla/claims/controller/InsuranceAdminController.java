package io.ctrla.claims.controller;

import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.invoiceDto.UploadInvoiceDtoResponse;
import io.ctrla.claims.dto.preauth.PreAuthDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.PreAuth;
import io.ctrla.claims.services.InsuranceService;
import io.ctrla.claims.services.PreAuthService;
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
    private final PreAuthService preAuthService;


    public InsuranceAdminController(InsuranceService insuranceService, PreAuthService preAuthService) {
        this.insuranceService = insuranceService;
        this.preAuthService = preAuthService;
    }


    //Get Insurance By ID
    @GetMapping("/{insuranceId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<InsuranceResponseDto>> getInsuranceById(@PathVariable Long insuranceId) {
        ApiResponse<InsuranceResponseDto> apiResponse = insuranceService.getInsuranceById(insuranceId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //Get Insurance Invoices
    @GetMapping("/{insuranceId}/invoices")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<UploadInvoiceDtoResponse>>> getInsuranceInvoices(@PathVariable Long insuranceId) {
        ApiResponse<List<UploadInvoiceDtoResponse>> apiResponse = insuranceService.getInsuranceInvoices(insuranceId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Update Insurance
    @PatchMapping("/{insuranceId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<InsuranceResponseDto>> updateOrder(@Valid @PathVariable Long insuranceId, @RequestBody InsuranceDto insuranceDto) {
        ApiResponse<InsuranceResponseDto> apiResponse = insuranceService.updateInsurance(insuranceId, insuranceDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Get PreAuths by insurance
    //Get All preauth of insurance
    @GetMapping("/{insuranceId}/preauth")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<PreAuth>>> getPreAuthsByInsurance(@PathVariable Long insuranceId) {
        ApiResponse<List<PreAuth>> apiResponse = preAuthService.getAllPreAuthsByInsurance(insuranceId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //update Insurance PreAuth
    @PatchMapping("/{insuranceId}/preauth/{preauthId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<PreAuth>> updatePreAuth(@Valid @PathVariable Long preauthId, @RequestBody PreAuthDto preAuthDto) {
        ApiResponse<PreAuth> apiResponse = preAuthService.updatePreAuth(preauthId,preAuthDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


}
