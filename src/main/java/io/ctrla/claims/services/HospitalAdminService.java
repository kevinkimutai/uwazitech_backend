package io.ctrla.claims.services;


import io.ctrla.claims.dto.hospital.HospitalAdminDto;
import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.hospitaladmin.HospitalAdminResponseDto;
import io.ctrla.claims.dto.invoiceDto.InvoiceDto;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.*;
import io.ctrla.claims.exceptions.NotFoundException;
import io.ctrla.claims.mappers.HospitalAdminMapper;
import io.ctrla.claims.repo.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class HospitalAdminService {

    private final HospitalAdminRepository hospitalAdminRepository;
    private final UserRepository userRepository;
    private final HospitalAdminMapper hospitalAdminMapper;
    private final PolicyHolderRepository policyHolderRepository;
    private final InsuranceRepository insuranceRepository;
    private final InvoiceRepository invoiceRepository;
    private final HospitalRepository hospitalRepository;
    private final PreAuthRepository preAuthRepository;
    private static final String UPLOAD_DIR = "uploads";

    public HospitalAdminService(HospitalAdminRepository hospitalAdminRepository,
                                HospitalAdminMapper hospitalAdminMapper,
                                UserRepository userRepository,
                                PolicyHolderRepository policyHolderRepository,
                                InsuranceRepository insuranceRepository,
                                InvoiceRepository invoiceRepository,
                                HospitalRepository hospitalRepository, PreAuthRepository preAuthRepository) {
        this.hospitalRepository = hospitalRepository;
        this.preAuthRepository = preAuthRepository;
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        this.invoiceRepository = invoiceRepository;
        this.insuranceRepository = insuranceRepository;
        this.policyHolderRepository = policyHolderRepository;
        this.hospitalAdminRepository = hospitalAdminRepository;
        this.userRepository = userRepository;
        this.hospitalAdminMapper = hospitalAdminMapper;

    }

    public ApiResponse<HospitalAdmin> getHospitalAdminById (Long hospitalAdminId){
        try {

            Optional<HospitalAdmin> optionalHospital = hospitalAdminRepository.findById(hospitalAdminId);

            // If hospital is present, get it, otherwise throw an exception
            HospitalAdmin hospitalAdmin = optionalHospital.orElseThrow(() -> new EntityNotFoundException("Hospital not found"));


            return new ApiResponse<>(200,"success",hospitalAdmin);
        }
        catch(NotFoundException e){
            return new ApiResponse<>(404, e.getMessage(), null);
        }
        catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching hospitalAdmin with Id", null);
        }

    }

    public ApiResponse<HospitalAdminResponseDto> updateHospitalAdmin(Long hospitalAdminId, HospitalAdminDto hospitalAdminDto) {
        try {

            HospitalAdmin foundHospitalAdmin = hospitalAdminRepository.findById(hospitalAdminId)
                    .orElseThrow(() -> new NotFoundException("No such hospitalAdmin with that Id"));

            User user = userRepository.findById(foundHospitalAdmin.getUser().getUserId())
                    .orElseThrow(() -> new NotFoundException("No such User with that Id"));;

            // Update only the fields that are provided in the DTO
            if (hospitalAdminDto.getIsVerified() != null) {
                user.setIsVerified(hospitalAdminDto.getIsVerified());
            }

            // Save the updated hospital to the database
            userRepository.save(user);

            HospitalAdminResponseDto hospitalAdminResponseDto =  hospitalAdminMapper.toHospitalAdminRes(foundHospitalAdmin);

                      return new ApiResponse<>(200, "success", hospitalAdminResponseDto);
        } catch (Exception e) {
            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while updating the hospital", null);
        }
    }

    public ApiResponse<Invoice> uploadInvoice(MultipartFile invoiceFile,
                                              String policyNumber,
                                              Long insuranceId,
                                              Long preauthId,
                                              String invoiceNumber,
                                              Long hospitalId) {
        try {



            // Validate the file
            if (invoiceFile == null || invoiceFile.isEmpty()) {
                return new ApiResponse<>(400, "missing invoice_file", null);
            }


            //Check if preauth is approved
         Optional<PreAuth> foundPreAuth =  preAuthRepository.findById(preauthId);
            if (foundPreAuth.isEmpty()) {
                return new ApiResponse<>(404, "PreAuth not found", null);
            }

            //Check if preauth is approved
            PreAuth preAuth = foundPreAuth.get();
            if (!preAuth.getIsApproved()){
                return new ApiResponse<>(400, "PreAuth not approved", null);
            }

                // Save the file locally
                String originalFilename = invoiceFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, originalFilename);
                Files.copy(invoiceFile.getInputStream(), filePath);

            //Get INsurance
            Insurance insurance = insuranceRepository.findById(insuranceId).get();

            //Get Hospital
             Hospital hospital = hospitalRepository.findById(hospitalId).get();

            //check if policyholder is there
            //if present.get POLicyHolder
            //if not create policyHolder
           Optional<PolicyHolder>pHolder = policyHolderRepository.findPolicyHolderByPolicyNumber(policyNumber);

           PolicyHolder policyHolder;
            if (pHolder.isPresent()) {
                // If present, get PolicyHolder
                policyHolder = pHolder.get();
            } else {
                // If not present, create a new PolicyHolder
                policyHolder = new PolicyHolder();
                policyHolder.setPolicyNumber(policyNumber);
                policyHolder.setInsurance(insurance);
                policyHolder = policyHolderRepository.save(policyHolder);
            }


            //Save to Db
            Invoice invoice = new Invoice();
            invoice.setInvoiceUrl(filePath.toString());
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setInsurance(insurance);
            invoice.setPolicyHolder(policyHolder);
            invoice.setHospital(hospital);
            invoice.setPreauth(preAuth);

          Invoice savedInvoice =  invoiceRepository.save(invoice);

//          //Send File to AI API
//            String aiApiUrl = "https://project-uwazitek.onrender.com/process-invoice";
//            RestTemplate restTemplate = new RestTemplate();
//
//            // Prepare the request with multipart data
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("invoice_file", new FileSystemResource(filePath.toFile()));
//
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//            // Make the request
//            ResponseEntity<String> response = restTemplate.postForEntity(aiApiUrl, requestEntity, String.class);
//
//            // Handle response
//            if (response.getStatusCode().is2xxSuccessful()) {
//                return new ApiResponse<>(200, "Invoice saved and file sent to AI API successfully", null);
//            } else {
//                return new ApiResponse<>(500, "Invoice saved but failed to send file to AI API", null);
//            }

                return new ApiResponse<>(200, "success", savedInvoice);
            } catch (Exception e) {
                // Return error response for unexpected errors
                return new ApiResponse<>(500, "An error occurred while updating the hospital", null);
            }
        }
}
