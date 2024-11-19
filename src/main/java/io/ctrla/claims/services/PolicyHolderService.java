package io.ctrla.claims.services;


import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.policyholder.CheckPwdPolicyHolder;
import io.ctrla.claims.dto.policyholder.PolicyHolderDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderRes;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.Invoice;
import io.ctrla.claims.entity.PolicyHolder;
import io.ctrla.claims.exceptions.NotFoundException;
import io.ctrla.claims.mappers.InsuranceMapper;
import io.ctrla.claims.mappers.PolicyHolderMapper;
import io.ctrla.claims.repo.InsuranceAdminRepository;
import io.ctrla.claims.repo.InsuranceRepository;
import io.ctrla.claims.repo.InvoiceRepository;
import io.ctrla.claims.repo.PolicyHolderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyHolderService {

    private final PolicyHolderRepository policyHolderRepository;
    private final PolicyHolderMapper policyHolderMapper;
    private final InvoiceRepository invoiceRepository;
    private final AuthService authService;


    public PolicyHolderService(
            PolicyHolderRepository policyHolderRepository,
            PolicyHolderMapper policyHolderMapper,
            InvoiceRepository invoiceRepository,
            @Lazy AuthService authService) {
        this.policyHolderMapper = policyHolderMapper;
        this.policyHolderRepository = policyHolderRepository;
        this.invoiceRepository = invoiceRepository;
        this.authService = authService;
    }

    //Get PolicyHolder
    public ApiResponse<PolicyHolderDto> getPolicyHolder() {
        try {

            Long userId = authService.getCurrentUserId();

            PolicyHolder pHolder = policyHolderRepository.findPolicyHolderByUserUserId(userId);
            PolicyHolderDto policyHolderDto = policyHolderMapper.toPolicyHolderDto(pHolder);


            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    policyHolderDto );
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching policy holder details", null);
        }
    }


    //Get PolicyHolders
    public ApiResponse<List<PolicyHolder>> getAllPolicyHolders() {
        try {

            List<PolicyHolder> pHolders = policyHolderRepository.findAll();
            //List<PolicyHolderDto> pHoldersDto = policyHolderMapper.toPolicyHoldersDto(pHolders);

            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    pHolders );
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching policy holder details", null);
        }
    }


//    //Get PolicyHolder By Id
//    public ApiResponse<PolicyHolderRes> getPolicyHolderById  (String policyHolderId){
//        try {
//
//            Optional<PolicyHolder> optionalPolicyHolder = policyHolderRepository.findPolicyHolderByPolicyNumber(policyHolderId);
//
//           // PolicyHolder policyHolder = optionalPolicyHolder.orElseThrow(() -> new EntityNotFoundException("PolicyHolder not found"));
//
//            // Map the hospital to the required result
//            PolicyHolderRes result = policyHolderMapper.toPolicyHolderRes(policyHolder);
//
//            return new ApiResponse<>(200,"success",result);
//        }
//        catch(NotFoundException e){
//            return new ApiResponse<>(404, e.getMessage(), null);
//        }
//        catch (Exception e) {
//
//            // Return error response for unexpected errors
//            return new ApiResponse<>(500, "An error occurred while fetching insurance with Id", null);
//        }

    //}


    //Check Pwd By PolicyNumber
    public ApiResponse<CheckPwdPolicyHolder> checkPwdPolicyHolderByPolicyNumber(String policyNumber) {
        try {

            PolicyHolder pHolder = policyHolderRepository.findPolicyHolderByPolicyNumber(policyNumber)
                    .orElseThrow(() -> new NotFoundException("No such PolicyHolder with that policyNumber"));;;


             CheckPwdPolicyHolder checkPwdPolicyHolder = new CheckPwdPolicyHolder();
             if(pHolder.getUser().getPassword()!=null){
                 checkPwdPolicyHolder.setHasPwd(true);
             }else{
                 checkPwdPolicyHolder.setHasPwd(false);
             }
            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    checkPwdPolicyHolder );
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while checking pwd policyholder details", null);
        }
    }

    //Get invoices
    public ApiResponse<List<Invoice>> getPolicyHolderInvoices(HttpServletRequest request) {
        try {
            Long userIdObj = (Long) request.getAttribute("userId");
            long userId = (userIdObj != null) ? userIdObj : 0L;

            PolicyHolder pHolder = policyHolderRepository.findPolicyHolderByUserUserId(userId);

            List<Invoice> invoices = pHolder.getInvoices();

            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    invoices);

        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching policy holder invoices", null);
        }
    }


    // Get a specific Invoice
    public ApiResponse<Invoice> getInvoice(HttpServletRequest request, Long invoiceId) {
        try {
            // Fetch the userId from the request
            Long userIdObj = (Long) request.getAttribute("userId");
            long userId = (userIdObj != null) ? userIdObj : 0L;

            // Fetch the policy holder using the userId
            PolicyHolder pHolder = policyHolderRepository.findPolicyHolderByUserUserId(userId);

            // Check if the policy holder exists
            if (pHolder == null) {
                throw new NotFoundException("Policy Holder not found for userId: " + userId);
            }

            // Fetch the specific invoice associated with the policy holder
            Invoice invoice = pHolder.getInvoices().stream()
                    .filter(inv -> inv.getInvoiceId().equals(invoiceId)) // Filter by invoiceId
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Invoice not found for invoiceId: " + invoiceId));

            // Return success response with the specific invoice
            return new ApiResponse<>(200, "success", invoice);

        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {
            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching the invoice", null);
        }
    }

}

