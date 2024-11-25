package io.ctrla.claims.dto.invoiceDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class InvoiceDto {
    @JsonProperty("invoice_number")
    private String invoiceNumber;

    @JsonProperty("policy_number")
    private String policyNumber;

    @JsonProperty("invoice_file")
    private MultipartFile invoiceFile;

    @JsonProperty("insurance_id")
    private Long insuranceId;

    @JsonProperty("preauth_id")
    private Long preauthId;
}
