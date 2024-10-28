package io.ctrla.claims.dto.insurance;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InsuranceResponseDto {
@JsonProperty("insurance_id")
    private Long insuranceId;
@JsonProperty("insurance_name")
    private String insuranceName;
@JsonProperty("created_at")
    private LocalDateTime createdAt;
 @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
