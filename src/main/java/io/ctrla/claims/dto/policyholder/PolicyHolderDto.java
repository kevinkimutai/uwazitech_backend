package io.ctrla.claims.dto.policyholder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.user.UserResponseDto;
import io.ctrla.claims.entity.Insurance;
import io.ctrla.claims.entity.User;
import jakarta.persistence.*;
import lombok.Data;


@Data
public class PolicyHolderDto {
    @JsonProperty("policy_holder_id")
    private Long policyHolderId;
    @JsonProperty("policy_number")
    private String policyNumber;

    private InsuranceResponseDto insurance;
    private UserResponseDto user;
}
