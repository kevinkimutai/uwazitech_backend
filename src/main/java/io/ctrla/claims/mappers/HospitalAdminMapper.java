package io.ctrla.claims.mappers;

import io.ctrla.claims.dto.hospital.HospitalAdminDto;
import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.hospitaladmin.HospitalAdminResponseDto;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.HospitalAdmin;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalAdminMapper {
    private final UserMapper userMapper;
    private final HospitalMapper hospitalMapper;

    public HospitalAdminMapper(UserMapper userMapper,HospitalMapper hospitalMapper) {
        this.userMapper = userMapper;
        this.hospitalMapper = hospitalMapper;
    }

    public HospitalAdminResponseDto toHospitalAdminRes(HospitalAdmin hospital){
        HospitalAdminResponseDto hospitalAdminRes = new HospitalAdminResponseDto();

        hospitalAdminRes.setHospitalAdminId(hospital.getHospitalAdminId());
        hospitalAdminRes.setUser(userMapper.toUserResDto( hospital.getUser()));
        hospitalAdminRes.setHospital(hospitalMapper.toHospitalRes(hospital.getHospital()));

        return hospitalAdminRes;
    }


    public List<HospitalAdminResponseDto> toHospitalAdminDto(List<HospitalAdmin> hospitals) {
        return hospitals.stream()
                .map(this::toHospitalAdminRes)
                .collect(Collectors.toList());
    }


}
