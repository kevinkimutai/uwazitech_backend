package io.ctrla.claims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthDto {
    @JsonProperty("jwt")
    private String token;
}
