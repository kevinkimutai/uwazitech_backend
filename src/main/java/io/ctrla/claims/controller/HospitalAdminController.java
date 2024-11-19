package io.ctrla.claims.controller;

import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.preauth.PreAuthDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.PreAuth;
import io.ctrla.claims.services.HospitalService;
import io.ctrla.claims.services.PreAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hospitaladmin")
public class HospitalAdminController {

    private final HospitalService hospitalService;
    private final PreAuthService preAuthService;

    public HospitalAdminController(HospitalService hospitalService,PreAuthService preAuthService) {
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


    //Update Hospital
    @PatchMapping("/{hospitalId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<HospitalResponseDto>> updateHospital(
            @Valid @PathVariable Long hospitalId, @RequestBody HospitalDto hospitalDto) {
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
    public ResponseEntity<ApiResponse<PreAuth>> createPreAuth(@Valid @RequestBody PreAuthDto preAuthDto, Long hospitalId) {
        ApiResponse<PreAuth> apiResponse = preAuthService.createPreAuth(preAuthDto,hospitalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


}
