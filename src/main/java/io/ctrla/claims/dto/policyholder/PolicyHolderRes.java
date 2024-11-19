package io.ctrla.claims.dto.policyholder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.entity.PolicyHolder;
import lombok.Data;

@Data
public class PolicyHolderRes {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("policy_number")
    private String policyNumber;
}
