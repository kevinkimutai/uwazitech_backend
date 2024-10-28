package io.ctrla.claims.dto.insurance;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
public class InsuranceDto {
    @JsonProperty("insurance_name")
    private String insuranceName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("contact_phone_number")
    private String phoneNumber;
}
