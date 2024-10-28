package io.ctrla.claims.controller;

import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.services.HospitalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hospitaladmin")
public class HospitalAdminController {

    private final HospitalService hospitalService;

    public HospitalAdminController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
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

}
