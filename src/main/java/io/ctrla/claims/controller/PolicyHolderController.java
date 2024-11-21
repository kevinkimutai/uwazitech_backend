package io.ctrla.claims.controller;


import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderRes;
import io.ctrla.claims.dto.policyholder.PolicyNumberDetails;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.Invoice;
import io.ctrla.claims.entity.PolicyHolder;
import io.ctrla.claims.services.InsuranceService;
import io.ctrla.claims.services.PolicyHolderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policyholder")
public class PolicyHolderController {
    private final PolicyHolderService policyHolderService;


    public PolicyHolderController(PolicyHolderService policyService) {

        this.policyHolderService = policyService;
    }

    //Get policyholder details
    @GetMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<PolicyNumberDetails>> getPolicyHolder() {
        ApiResponse<PolicyNumberDetails> apiResponse = policyHolderService.getPolicyHolder();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    //Get policyholder details
//    @GetMapping("/{policyHolderId}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<ApiResponse<PolicyHolderRes>> getPolicyHolderById(@PathVariable("policyHolderId") Long policyHolderId) {
//        ApiResponse<PolicyHolderRes> apiResponse = policyHolderService.getPolicyHolderById(policyHolderId);
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
//    }


//    //Get PolicyHolders Invoices
//    @GetMapping("/invoices")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<ApiResponse<List<Invoice>>> getPolicyHoldersInvoices(HttpServletRequest request) {
//        ApiResponse<List<Invoice>> apiResponse = policyHolderService.getPolicyHolderInvoices(request);
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
//    }
//
//    //Get Invoice By InvoiceId
//    @GetMapping(value = "/invoices/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<ApiResponse<Invoice>> createInsuranceCompany(@PathVariable Long invoiceId,HttpServletRequest request) {
//        ApiResponse<Invoice> apiResponse = policyHolderService.getInvoice(request,invoiceId);
//        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
//    }
}