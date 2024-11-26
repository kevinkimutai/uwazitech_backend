package io.ctrla.claims.dto.preauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PreAuthDetails {
    @JsonProperty("preauth_id")
    private Long preAuthId;
    @JsonProperty("policy_number")
    private String policyNumber;
    @JsonProperty("is_approved")
    private Boolean isApproved;
}
