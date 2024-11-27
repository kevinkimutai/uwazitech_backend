package io.ctrla.claims.mappers;

import io.ctrla.claims.dto.hospitaladmin.HospitalAdminResponseDto;
import io.ctrla.claims.dto.invoiceDto.UploadInvoiceDtoResponse;
import io.ctrla.claims.entity.HospitalAdmin;
import io.ctrla.claims.entity.Invoice;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceMapper {

    private final PolicyHolderMapper policyHolderMapper;
    private final InsuranceMapper insuranceMapper;
    private final HospitalMapper hospitalMapper;
    private final PreAuthMapper preAuthMapper;

    @Lazy
    public InvoiceMapper(PolicyHolderMapper policyHolderMapper, InsuranceMapper insuranceMapper, HospitalMapper hospitalMapper, PreAuthMapper preAuthMapper) {
        this.policyHolderMapper = policyHolderMapper;
        this.insuranceMapper = insuranceMapper;
        this.hospitalMapper = hospitalMapper;
        this.preAuthMapper = preAuthMapper;
    }

    public UploadInvoiceDtoResponse toInvoiceDtoRes(Invoice invoice) {
        UploadInvoiceDtoResponse invoiceRes = new UploadInvoiceDtoResponse();

        invoiceRes.setInvoiceId(invoice.getInvoiceId());
        invoiceRes.setInvoiceNumber(invoice.getInvoiceNumber());
        invoiceRes.setInvoiceUrl(invoice.getInvoiceUrl());
        invoiceRes.setPolicyHolder(policyHolderMapper.toPolicyHolderDto(invoice.getPolicyHolder()));
        invoiceRes.setInsurance(insuranceMapper.toInsuranceRes(invoice.getInsurance()));
        invoiceRes.setHospital(hospitalMapper.toHospitalRes(invoice.getHospital()));
        invoiceRes.setPreauth(preAuthMapper.toPreAuthDetails(invoice.getPreauth()));
        invoiceRes.setInvoiceItems(invoice.getInvoiceItems());
        invoiceRes.setBankName(invoice.getBankName());
        invoiceRes.setBankAccountNumber(invoice.getBankAccountNumber());

        return invoiceRes;
    }

    public List<UploadInvoiceDtoResponse> toInvoiceDtoResList(List<Invoice> invoices) {
        List<UploadInvoiceDtoResponse> invoiceResponses = new ArrayList<>();

        for (Invoice invoice : invoices) {
            UploadInvoiceDtoResponse invoiceRes = toInvoiceDtoRes(invoice);
            invoiceResponses.add(invoiceRes);
        }

        return invoiceResponses;
    }



}
