package io.ctrla.claims.mappers;

import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.preauth.PreAuthDetails;
import io.ctrla.claims.dto.preauth.PreAuthDto;
import io.ctrla.claims.dto.preauth.PreAuthResponseDto;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.PreAuth;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreAuthMapper {
    private final PolicyHolderMapper policyHolderMapper;
    private final HospitalMapper hospitalMapper;
    private final InsuranceMapper insuranceMapper;

    public PreAuthMapper(PolicyHolderMapper policyHolderMapper, HospitalMapper hospitalMapper, InsuranceMapper insuranceMapper) {
        this.policyHolderMapper = policyHolderMapper;
        this.hospitalMapper = hospitalMapper;
        this.insuranceMapper = insuranceMapper;
    }

    public PreAuth toPreAuth(PreAuthDto preAuthDto){
        PreAuth preAuth = new PreAuth();
        preAuth.setHospital(preAuthDto.getHospital());
        preAuth.setInsurance(preAuthDto.getInsurance());
        preAuth.setPolicyHolder(preAuthDto.getPolicyHolder());

        return preAuth;

    }

    public PreAuthDetails toPreAuthDetails(PreAuth preAuth){
        PreAuthDetails preAuthDetails = new PreAuthDetails();
        preAuthDetails.setPreAuthId (preAuth.getPreAuthId());
        preAuthDetails.setPolicyNumber(preAuthDetails.getPolicyNumber());
        preAuthDetails.setIsApproved(preAuthDetails.getIsApproved());


        return preAuthDetails;

    }

    public PreAuthResponseDto toPreAuthRes(PreAuth preAuth){
        PreAuthResponseDto preAuthRes = new PreAuthResponseDto();
        preAuthRes.setPreAuthId(preAuth.getPreAuthId());
        preAuthRes.setPolicyHolder(policyHolderMapper.toPolicyHolderRes(preAuth.getPolicyHolder()));
        preAuthRes.setHospital(hospitalMapper.toHospitalRes(preAuth.getHospital()));
        preAuth.setIsApproved(preAuth.getIsApproved());
        preAuth.setInsurance(preAuth.getInsurance());


        return preAuthRes;

    }

    public List<PreAuthResponseDto> toPreAuthsRes(List<PreAuth> preAuths) {
        return preAuths.stream()
                .map(this::toPreAuthRes)
                .collect(Collectors.toList());

    }

}
