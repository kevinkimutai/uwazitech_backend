package io.ctrla.claims.dto.invoiceDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderDto;
import io.ctrla.claims.dto.preauth.PreAuthDetails;
import io.ctrla.claims.dto.preauth.PreAuthResponseDto;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.Insurance;
import io.ctrla.claims.entity.PolicyHolder;
import io.ctrla.claims.entity.PreAuth;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class UploadInvoiceDtoResponse {
    @JsonProperty("invoice_id")
    private Long invoiceId;

    @JsonProperty("invoice_number")
    private String invoiceNumber;

    @JsonProperty("invoice_url")
    private String invoiceUrl;

    @JsonProperty("policyholder")
    private PolicyHolderDto policyHolder;

    private InsuranceResponseDto insurance;

    private HospitalResponseDto hospital;

    private PreAuthDetails preauth;

}
