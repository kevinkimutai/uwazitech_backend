package io.ctrla.claims.mappers;

import io.ctrla.claims.dto.hospitaladmin.HospitalAdminResponseDto;
import io.ctrla.claims.dto.insuranceadmin.InsuranceAdminResponseDto;
import io.ctrla.claims.entity.HospitalAdmin;
import io.ctrla.claims.entity.InsuranceAdmin;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class InsuranceAdminMapper {
    private final UserMapper userMapper;
    private final InsuranceMapper insuranceMapper;

    public InsuranceAdminMapper(UserMapper userMapper,InsuranceMapper insuranceMapper) {
        this.userMapper = userMapper;
        this.insuranceMapper = insuranceMapper;
    }

    public InsuranceAdminResponseDto toInsuranceAdminRes(InsuranceAdmin insuranceAdmin){
        InsuranceAdminResponseDto insuranceAdminRes = new InsuranceAdminResponseDto();

        insuranceAdminRes.setInsuranceAdminId(insuranceAdmin.getInsuranceAdminId());
        insuranceAdminRes.setUser(userMapper.toUserResDto( insuranceAdmin.getUser()));
        insuranceAdminRes.setInsurance(insuranceMapper.toInsuranceRes(insuranceAdmin.getInsurance()));

        return insuranceAdminRes;
    }


    public List<InsuranceAdminResponseDto> toInsuranceAdminDto(List<InsuranceAdmin> insuranceAdmins) {
        return insuranceAdmins.stream()
                .map(this::toInsuranceAdminRes)
                .collect(Collectors.toList());
    }

}
