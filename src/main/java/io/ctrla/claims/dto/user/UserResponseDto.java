package io.ctrla.claims.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class UserResponseDto {
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

}
