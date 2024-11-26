package io.ctrla.claims.dto.preauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderDetails;
import io.ctrla.claims.dto.policyholder.PolicyHolderRes;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.Insurance;
import io.ctrla.claims.entity.PolicyHolder;
import lombok.Data;

@Data
public class PreAuthResponseDto {
    @JsonProperty("preauth_id")
    private Long preAuthId;
    @JsonProperty("insurance_id")
    private Long insuranceId;
    @JsonProperty("policy_number")
    private String policyNumber;
    private HospitalResponseDto hospital;
    private InsuranceResponseDto insurance;
    private PolicyHolderDetails policyHolder;
}
