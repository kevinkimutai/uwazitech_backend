package io.ctrla.claims.dto.hospitaladmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import lombok.Data;


@Data
public class HospitalAdminResponseDto {
    @JsonProperty("hospitalAdminId")
    private Long hospitalAdminId;

    @JsonProperty("user")
    private HospitalAdminUserDto user;

    @JsonProperty("hospital")
    private HospitalResponseDto hospital;

}
