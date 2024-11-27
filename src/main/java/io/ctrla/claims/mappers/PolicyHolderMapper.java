package io.ctrla.claims.mappers;


import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insuranceadmin.InsuranceAdminResponseDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderDetails;
import io.ctrla.claims.dto.policyholder.PolicyHolderDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderRes;
import io.ctrla.claims.dto.policyholder.PolicyNumberDetails;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.InsuranceAdmin;
import io.ctrla.claims.entity.PolicyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PolicyHolderMapper {

    private final InsuranceMapper insuranceMapper;
    private final UserMapper userMapper;

    public PolicyHolderMapper(InsuranceMapper insuranceMapper,UserMapper userMapper) {
        this.insuranceMapper = insuranceMapper;
        this.userMapper = userMapper;
    }

    public PolicyHolderDto toPolicyHolderDto(PolicyHolder policyHolder) {
        PolicyHolderDto policyHolderDto = new PolicyHolderDto();

        policyHolderDto.setPolicyHolderId(policyHolder.getPolicyHolderId());
        policyHolderDto.setPolicyNumber(policyHolder.getPolicyNumber());
        policyHolderDto.setUser(userMapper.toUserResponseDto(policyHolder.getUser()));
        policyHolderDto.setInsurance(insuranceMapper.toInsuranceRes(policyHolder.getInsurance()));

        return policyHolderDto;
    }

    public List<PolicyHolderDto> toPolicyHoldersDto(List<PolicyHolder> policyHolders) {
        return policyHolders.stream()
                .map(this::toPolicyHolderDto)
                .collect(Collectors.toList());

    }


    public PolicyHolderDetails toPolicyHolderRes(PolicyHolder policyHolder){
        PolicyHolderDetails policyHolderDetails = new PolicyHolderDetails();

        policyHolderDetails.setPolicyHolderId(policyHolder.getPolicyHolderId());
        policyHolderDetails.setUser(userMapper.toUserResDto( policyHolder.getUser()));
        policyHolderDetails.setInsurance(insuranceMapper.toInsuranceRes(policyHolder.getInsurance()));

        return policyHolderDetails;
    }


    public List<PolicyHolderDetails> toPolicyHolderDetailsDto(List<PolicyHolder> pHolders) {
        return pHolders.stream()
                .map(this::toPolicyHolderRes)
                .collect(Collectors.toList());
    }

    public PolicyNumberDetails toPolicyNumber(PolicyHolder policyHolder){
        PolicyNumberDetails pNumber = new PolicyNumberDetails();

        pNumber.setPolicyNumber(policyHolder.getPolicyNumber());
        pNumber.setUser(userMapper.toUserResDto( policyHolder.getUser()));
        pNumber.setInsurance(insuranceMapper.toInsuranceRes(policyHolder.getInsurance()));
        pNumber.setPolicyHolderId(policyHolder.getPolicyHolderId());
        pNumber.setInvoice(policyHolder.getInvoices());

        return pNumber;
    }


}
