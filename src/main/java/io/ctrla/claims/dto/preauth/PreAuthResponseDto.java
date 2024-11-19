package io.ctrla.claims.dto.preauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.Insurance;
import io.ctrla.claims.entity.PolicyHolder;

public class PreAuthResponseDto {
    @JsonProperty("preauth_id")
    private Long preAuthId;
    @JsonProperty("insurance_id")
    private Long insuranceId;
    @JsonProperty("policy_number")
    private String policyNumber;
    @JsonProperty("policy_number")
    private Hospital hospital;
    private Insurance insurance;
    private PolicyHolder policyHolder;
}
