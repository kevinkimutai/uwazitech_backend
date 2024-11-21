package io.ctrla.claims.dto.insuranceadmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.hospitaladmin.HospitalAdminUserDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import lombok.Data;

@Data
public class InsuranceAdminResponseDto {
    @JsonProperty("insuranceAdminId")
    private Long insuranceAdminId;

    @JsonProperty("user")
    private HospitalAdminUserDto user;

    @JsonProperty("insurance")
    private InsuranceResponseDto insurance;
}
