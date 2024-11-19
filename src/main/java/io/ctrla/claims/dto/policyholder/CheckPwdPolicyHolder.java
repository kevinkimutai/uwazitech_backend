package io.ctrla.claims.dto.policyholder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class CheckPwdPolicyHolder {
    @JsonProperty("has_pwd")
    private Boolean hasPwd;
}
