package io.ctrla.claims.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ctrla.claims.dto.hospital.HospitalAdminDto;
import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.hospitaladmin.HospitalAdminResponseDto;
import io.ctrla.claims.dto.invoiceDto.InvoiceDto;
import io.ctrla.claims.dto.invoiceDto.UploadInvoiceDtoResponse;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.*;
import io.ctrla.claims.exceptions.NotFoundException;
import io.ctrla.claims.mappers.HospitalAdminMapper;
import io.ctrla.claims.mappers.InvoiceMapper;
import io.ctrla.claims.repo.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
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
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemRepository invoiceItemRepository;
    private static final String UPLOAD_DIR = "uploads";

    public HospitalAdminService(HospitalAdminRepository hospitalAdminRepository,
                                HospitalAdminMapper hospitalAdminMapper,
                                UserRepository userRepository,
                                PolicyHolderRepository policyHolderRepository,
                                InsuranceRepository insuranceRepository,
                                InvoiceRepository invoiceRepository,
                                HospitalRepository hospitalRepository, PreAuthRepository preAuthRepository, InvoiceMapper invoiceMapper, InvoiceItemRepository invoiceItemRepository) {
        this.hospitalRepository = hospitalRepository;
        this.preAuthRepository = preAuthRepository;
        this.invoiceMapper = invoiceMapper;
        this.invoiceItemRepository = invoiceItemRepository;
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

    public ApiResponse<UploadInvoiceDtoResponse> uploadInvoice(MultipartFile invoiceFile,
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
            //Check if preauth is Present
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
            // Generate a timestamp to make the filename unique
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            // Extract the file extension from the original filename
            assert originalFilename != null;
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // Create a new filename by adding the timestamp
            String newFilename = timestamp + "_" + originalFilename;
            // Set the file path with the new unique filename
            Path filePath = Paths.get(UPLOAD_DIR, newFilename);



            // Save the file
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
          UploadInvoiceDtoResponse uploadedInvoice = invoiceMapper.toInvoiceDtoRes(savedInvoice);



          //Send File to AI API
            String aiApiUrl = "https://project-uwazitek.onrender.com/process-invoice";
            String aiGetResultsApiUrl = "https://project-uwazitek.onrender.com/generate-report";
            RestTemplate restTemplate = new RestTemplate();

            // Prepare the request with multipart data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("invoice_file", new FileSystemResource(filePath.toFile()));


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Make the request
            System.out.println("SENDING FORM TO AI ");
            ResponseEntity<String> response = restTemplate.postForEntity(aiApiUrl, requestEntity, String.class);

            // Handle response
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("SUCCESS ");
                System.out.println("GETTING AI DATA ");
                //GENERATE DATA
                ResponseEntity<String> aiRes = restTemplate.getForEntity(aiGetResultsApiUrl, String.class);
                // Check the response status and return the body if successful
                if (aiRes.getStatusCode().is2xxSuccessful()) {
                    System.out.println("DATA RETURNED IS :"+aiRes.getBody());

                    //MAP ITEMS TO INVOICE ITEM
                    // Parse the JSON response
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(aiRes.getBody());

                    // Fetch "Fraud Detection Results"
                    JsonNode fraudDetectionResults = rootNode.path("Fraud Detection Results");

                    if (fraudDetectionResults.isArray()) {
                        for (JsonNode item : fraudDetectionResults) {
                            // Skip invalid entries
                            if (item.path("Base Cost").isNull() || item.path("Description").isNull()) {
                                continue;
                            }

                            // Map data to InvoiceItem entity
                            InvoiceItem invoiceItem = new InvoiceItem();
                            invoiceItem.setInvoiceItemName(item.path("Description").asText());
                            invoiceItem.setInvoiceItemPrice(item.path("Invoice Cost").asDouble());
                            invoiceItem.setFraudLevel(item.path("Fraud Category").asText());
                            invoiceItem.setInvoiceItemPriceDifference(item.path("Price Difference").asDouble());
                            invoiceItem.setInvoice(savedInvoice);

                            // Assign the associated Invoice (assume the invoice object is available)
                            invoiceItem.setInvoice(invoice); // Ensure "invoice" is properly fetched or passed to this method

                            // Save the InvoiceItem
                            invoiceItemRepository.save(invoiceItem);
                        }
                    }


                } else {
                    throw new RuntimeException("Failed to fetch data. HTTP Status: " + response.getStatusCode());
                }

            } else {
                return new ApiResponse<>(500, "Invoice saved but failed to send file to AI API", null);
            }

                return new ApiResponse<>(200, "success", uploadedInvoice);
            } catch (Exception e) {
                // Return error response for unexpected errors
              log.error("errrrrrror: ", e);
                return new ApiResponse<>(500, "An error occurred while updating the hospital", null);
            }
        }
}
