package io.ctrla.claims.mappers;

import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.entity.Insurance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceMapper {

    public InsuranceResponseDto toInsuranceRes(Insurance insurance){
        InsuranceResponseDto insuranceRes = new InsuranceResponseDto();
        insuranceRes.setInsuranceId(insurance.getInsuranceId());
        insuranceRes.setInsuranceName(insurance.getInsuranceName());
        insuranceRes.setAddress(insuranceRes.getAddress());
        insuranceRes.setPhoneNumber(insurance.getPhoneNumber());
        insuranceRes.setCreatedAt(insurance.getCreatedAt());
        insuranceRes.setUpdatedAt(insurance.getUpdatedAt());

        return insuranceRes;
    }



    public List<InsuranceResponseDto>  toInsuranceDto(List<Insurance> insurances) {
        return insurances.stream()
                .map(this::toInsuranceRes)
                .collect(Collectors.toList());

    }

    public Insurance toInsurance(InsuranceDto insuranceDto) {
        Insurance insurance = new Insurance();
        insurance.setInsuranceName(insuranceDto.getInsuranceName());
        insurance.setInsuranceAddress(insuranceDto.getAddress());
        insurance.setPhoneNumber(insuranceDto.getPhoneNumber());
        return insurance;
    }

}
