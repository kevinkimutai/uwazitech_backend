package io.ctrla.claims.dto.policyholder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.dto.hospitaladmin.HospitalAdminUserDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import lombok.Data;

@Data
public class PolicyHolderDetails {
    @JsonProperty("policyHolderId")
    private Long policyHolderId;

    @JsonProperty("user")
    private HospitalAdminUserDto user;

    @JsonProperty("insurance")
    private InsuranceResponseDto insurance;
}
