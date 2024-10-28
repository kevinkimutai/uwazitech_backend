package io.ctrla.claims.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.entity.Roles;
import io.ctrla.claims.entity.SignUpTypes;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserDto {
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;
    private String password;

    @JsonProperty("hospital_id")
    private Long hospitalId;

    @JsonProperty("insurance_id")
    private Long insuranceId;

    @JsonProperty("signup_type")
    @Enumerated(EnumType.STRING)
    private String signUpType;

}



