package io.ctrla.claims.controller;

import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.invoiceDto.InvoiceDto;
import io.ctrla.claims.dto.preauth.PreAuthDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.Invoice;
import io.ctrla.claims.entity.PreAuth;
import io.ctrla.claims.services.HospitalAdminService;
import io.ctrla.claims.services.HospitalService;
import io.ctrla.claims.services.PreAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hospitaladmin")
public class HospitalAdminController {

    private final HospitalService hospitalService;
    private final PreAuthService preAuthService;
    private final HospitalAdminService hospitalAdminService;

    public HospitalAdminController(HospitalService hospitalService, PreAuthService preAuthService, HospitalAdminService hospitalAdminService) {

        this.hospitalAdminService = hospitalAdminService;
        this.hospitalService = hospitalService;
        this.preAuthService = preAuthService;

    }

    //Get Hospital By ID
    @GetMapping("/{hospitalId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<HospitalResponseDto>> getHospitalById(@PathVariable Long hospitalId) {
        ApiResponse<HospitalResponseDto> apiResponse = hospitalService.getHospitalById(hospitalId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Upload Invoice
    @PostMapping("/{hospitalId}/invoice/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)

    public ResponseEntity<ApiResponse<Invoice>> uploadInvoice(
            @RequestPart("invoice_file") MultipartFile invoiceFile,
            @RequestPart("policy_number") String policyNumber,
            @RequestPart("insurance_id") Long insuranceId,
            @RequestPart("preauth_id") Long preauthId,
            @RequestPart("invoice_number") String invoiceNumber,
            @PathVariable Long hospitalId
    ) {
        System.out.println("REHE............");
        ApiResponse<Invoice> apiResponse = hospitalAdminService.uploadInvoice(
                invoiceFile,
                policyNumber,
                insuranceId,
                preauthId,
                invoiceNumber,
                hospitalId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Update Hospital
    @PatchMapping("/{hospitalId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<HospitalResponseDto>> updateHospital(
           @PathVariable Long hospitalId,@Valid  @RequestBody HospitalDto hospitalDto) {
        ApiResponse<HospitalResponseDto> apiResponse = hospitalService.updateHospital( hospitalId, hospitalDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }


    //PreAuths
    //Get All preauth of hospital
    @GetMapping("/{hospitalId}/preauth")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<PreAuth>>> getPreAuthById(@PathVariable Long hospitalId) {
        ApiResponse<List<PreAuth>> apiResponse = preAuthService.getAllPreAuthsByHospital(hospitalId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Create PreAuth
    @PostMapping(value = "/{hospitalId}/preauth")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<PreAuth>> createPreAuth(@Valid @RequestBody PreAuthDto preAuthDto,@PathVariable Long hospitalId) {
        ApiResponse<PreAuth> apiResponse = preAuthService.createPreAuth(preAuthDto,hospitalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


}
