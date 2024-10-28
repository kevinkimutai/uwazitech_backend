package io.ctrla.claims.mappers;


import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.policyholder.PolicyHolderDto;
import io.ctrla.claims.entity.Hospital;
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

}
