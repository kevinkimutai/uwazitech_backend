package io.ctrla.claims.dto.hospital;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;


@Data
public class HospitalDto {

    @JsonProperty("hospital_address")
    private String hospitalAddress;

    @JsonProperty("hospital_branch")
    private String hospitalBranch;

    @JsonProperty("hospital_name")
    private String hospitalName;
}
