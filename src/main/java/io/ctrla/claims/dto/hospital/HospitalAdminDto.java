package io.ctrla.claims.dto.hospital;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HospitalAdminDto {
    @JsonProperty("is_verified")
    private Boolean isVerified;
}
