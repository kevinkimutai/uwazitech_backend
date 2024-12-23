package io.ctrla.claims.services;


import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.dto.insuranceadmin.InsuranceAdminResponseDto;
import io.ctrla.claims.dto.invoiceDto.UploadInvoiceDtoResponse;
import io.ctrla.claims.dto.response.ApiResponse;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.HospitalAdmin;
import io.ctrla.claims.entity.Insurance;
import io.ctrla.claims.entity.InsuranceAdmin;
import io.ctrla.claims.exceptions.NotFoundException;
import io.ctrla.claims.mappers.InsuranceAdminMapper;
import io.ctrla.claims.mappers.InsuranceMapper;
import io.ctrla.claims.mappers.InvoiceMapper;
import io.ctrla.claims.repo.InsuranceAdminRepository;
import io.ctrla.claims.repo.InsuranceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final InsuranceMapper insuranceMapper;
    private final InsuranceAdminRepository insuranceAdminRepository;
    private  final InsuranceAdminMapper insuranceAdminMapper;
    private final InvoiceMapper invoiceMapper;

    public InsuranceService(InsuranceRepository insuranceRepository, InsuranceAdminMapper insuranceAdminMapper, InsuranceMapper insuranceMapper, InsuranceAdminRepository insuranceAdminRepository, InvoiceMapper invoiceMapper) {
        this.insuranceRepository = insuranceRepository;
        this.insuranceMapper = insuranceMapper;
        this.insuranceAdminRepository = insuranceAdminRepository;
        this.insuranceAdminMapper = insuranceAdminMapper;
        this.invoiceMapper = invoiceMapper;
    }

    //Get All Registered Insurance Companies
    public ApiResponse<List<InsuranceResponseDto>> getAllInsurance() {
        try {
//            // Get the userId and role from the request attributes set by JwtFilter
//            String role = (String) request.getAttribute("role");
//
//            // If the role is ROLE_ADMIN, skip the hospital admin check
//            if (!"ROLE_ADMIN".equals(role)) {
//                // If not, return a forbidden response
//                return new ApiResponse<>(403, "unauthorised", null);
//            }

            List<InsuranceResponseDto> insurances = insuranceMapper.toInsuranceDto(insuranceRepository.findAll());

            if (insurances.isEmpty()) {
                throw new NotFoundException("No insurance companies found");
            }

            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    insurances);
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching insurance companies", null);
        }
    }

    //Get InsuranceCompany By Id
    public ApiResponse<InsuranceResponseDto> getInsuranceById (Long insuranceId){
        try {
//
//            // Get the userId and role from the request attributes set by JwtFilter
//            Long authenticatedUserId = (Long) request.getAttribute("userId");
//            String role = (String) request.getAttribute("role");
//
//            // If the role is ROLE_ADMIN, skip the hospital admin check
//            if (!"ROLE_ADMIN".equals(role)) {
//                InsuranceAdmin insuranceAdmin = insuranceAdminRepository.findByUserUserId(authenticatedUserId);
//
//                // Check if the authenticated hospital admin is trying to update their own hospital
//                if (insuranceAdmin == null || !insuranceAdmin.getInsurance().getInsuranceId().equals(insuranceId)) {
//                    // If not, return a forbidden response
//                    return new ApiResponse<>(403, "You are not authorized to update this hospital", null);
//                }
//            }
//
//

            Optional<Insurance> optionalInsurance = insuranceRepository.findById(insuranceId);

            // If insurance is present, get it, otherwise throw an exception
            Insurance insurance = optionalInsurance.orElseThrow(() -> new EntityNotFoundException("Insurance not found"));

            // Map the insurance to the required result
            InsuranceResponseDto result = insuranceMapper.toInsuranceRes(insurance);

            return new ApiResponse<>(200,"success",result);
        }
        catch(NotFoundException e){
            return new ApiResponse<>(404, e.getMessage(), null);
        }
        catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching insurance with Id", null);
        }

    }

    //Create Insurance Company
    public ApiResponse<InsuranceResponseDto> createInsurance(InsuranceDto insuranceDto){
        try{


        Insurance newInsurance = insuranceRepository.save(insuranceMapper.toInsurance(insuranceDto));
        InsuranceResponseDto result = insuranceMapper.toInsuranceRes(newInsurance);

           return new ApiResponse<>(200,"success",result);
        }
        catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching insurance with Id", null);
        }
    }

    //Public Update
    public ApiResponse<InsuranceResponseDto> updateInsurance(Long insuranceId, InsuranceDto insuranceDto){
        try{
//            // Get the userId and role from the request attributes set by JwtFilter
//            Long authenticatedUserId = (Long) request.getAttribute("userId");
//            String role = (String) request.getAttribute("role");
//
//            // If the role is ROLE_ADMIN, skip the hospital admin check
//            if (!"ROLE_ADMIN".equals(role)) {
//                InsuranceAdmin insuranceAdmin = insuranceAdminRepository.findByUserUserId(authenticatedUserId);
//
//                // Check if the authenticated hospital admin is trying to update their own hospital
//                if (insuranceAdmin == null || !insuranceAdmin.getInsurance().getInsuranceId().equals(insuranceId)) {
//                    // If not, return a forbidden response
//                    return new ApiResponse<>(403, "You are not authorized to update this hospital", null);
//                }
//            }

           //Proceed
            Insurance foundInsurance = insuranceRepository.findById(insuranceId)
                    .orElseThrow(() -> new NotFoundException("No such insurance with that Id"));

            // Update only the fields that are provided in the DTO
            if (insuranceDto.getInsuranceName() != null) {
                foundInsurance.setInsuranceName(insuranceDto.getInsuranceName());
            }

            // Save the updated order to the database
            InsuranceResponseDto result = insuranceMapper.toInsuranceRes(insuranceRepository.save(foundInsurance));

            return new ApiResponse<>(200,"success",result);
        }
        catch (Exception e) {
            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching insurance with Id", null);
        }
    }

    //Delete insurance
    public void deleteInsurance(Long insuranceId) {
        try {
            insuranceRepository.deleteById(insuranceId);
        } catch (EmptyResultDataAccessException e) {
            // Handle case where insurance with the given ID does not exist
            throw new EntityNotFoundException("Insurance with ID " + insuranceId + " not found.");
        } catch (DataIntegrityViolationException e) {
            // Handle cases where deleting the insurance violates database integrity (e.g., foreign key constraints)
            throw new RuntimeException("Cannot delete insurance with ID " + insuranceId + " due to data integrity violation.");
        } catch (Exception e) {
            // Log and rethrow unexpected exceptions for further handling
            throw new RuntimeException("An unexpected error occurred while trying to delete insurance with ID " + insuranceId, e);
        }
    }


    //Get Insurance Admins
    public ApiResponse<List<InsuranceAdminResponseDto>> getInsuranceAdmins() {
        try {
            List<Insurance> insurances = insuranceRepository.findAll();
            if (insurances.isEmpty()) {
                throw new NotFoundException("No insurance companies found");
            }

            List<InsuranceAdmin> insuranceAdmins = insurances.stream()
                    .flatMap(hospital -> hospital.getInsuranceAdmins().stream())
                    .collect(Collectors.toList());

List<InsuranceAdminResponseDto> insuranceResponseDtos = insuranceAdminMapper.toInsuranceAdminDto(insuranceAdmins);
            // Return success response
            return new ApiResponse<>(
                    200,
                    "success",
                    insuranceResponseDtos);
        } catch (NotFoundException nfe) {
            // Handle not found scenario
            return new ApiResponse<>(404, nfe.getMessage(), null);

        } catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching insurance admins", null);
        }
    }

    //Get Insurance Admins
    public ApiResponse<List<UploadInvoiceDtoResponse>> getInsuranceInvoices(Long insuranceId) {
        try{
            Optional<Insurance> optionalInsurance = insuranceRepository.findById(insuranceId);

            // If hospital is present, get it, otherwise throw an exception
            Insurance insurance = optionalInsurance.orElseThrow(() -> new EntityNotFoundException("Insurance not found"));

            // Map the hospital to the required result
            List<UploadInvoiceDtoResponse> result = invoiceMapper.toInvoiceDtoResList(insurance.getInvoices());

            return new ApiResponse<>(200,"success",result);
        }
        catch(NotFoundException e){
            return new ApiResponse<>(404, e.getMessage(), null);
        }
        catch (Exception e) {

            // Return error response for unexpected errors
            return new ApiResponse<>(500, "An error occurred while fetching insurance with Id", null);
        }

    }


}
