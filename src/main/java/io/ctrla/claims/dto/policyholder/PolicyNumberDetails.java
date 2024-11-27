package io.ctrla.claims.dto.policyholder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.dto.hospitaladmin.HospitalAdminUserDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.entity.Invoice;
import lombok.Data;

import java.util.List;

@Data
public class PolicyNumberDetails {
    @JsonProperty("policyHolderId")
    private Long policyHolderId;

    @JsonProperty("policy_number")
    private String policyNumber;

    @JsonProperty("user")
    private HospitalAdminUserDto user;

    @JsonProperty("insurance")
    private InsuranceResponseDto insurance;

    private List<Invoice> invoice;
}
