package io.ctrla.claims.dto.hospital;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class HospitalResponseDto {
    private Long hospitalId;
    private String hospitalName;
    private String hospitalBranch;
    private String hospitalAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
