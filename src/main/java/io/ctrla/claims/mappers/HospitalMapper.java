package io.ctrla.claims.mappers;

import io.ctrla.claims.dto.hospital.HospitalDto;
import io.ctrla.claims.dto.hospital.HospitalResponseDto;
import io.ctrla.claims.dto.insurance.InsuranceDto;
import io.ctrla.claims.dto.insurance.InsuranceResponseDto;
import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.Insurance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class HospitalMapper {


    public HospitalResponseDto toHospitalRes(Hospital hospital){
        HospitalResponseDto hospitalRes = new HospitalResponseDto();

        hospitalRes.setHospitalId(hospital.getHospitalId());
        hospitalRes.setHospitalName(hospital.getHospitalName());
        hospitalRes.setHospitalAddress(hospital.getHospitalAddress());
        hospitalRes.setCreatedAt(hospital.getCreatedAt());
        hospitalRes.setUpdatedAt(hospital.getUpdatedAt());

        return hospitalRes;
    }



    public List<HospitalResponseDto> toHospitalDto(List<Hospital> hospitals) {
        return hospitals.stream()
                .map(this::toHospitalRes)
                .collect(Collectors.toList());

    }

    public Hospital toHospital(HospitalDto hospitalDto) {
        Hospital hospital = new Hospital();
        hospital.setHospitalName(hospitalDto.getHospitalName());
        hospital.setHospitalBranch(hospitalDto.getHospitalBranch());
        hospital.setHospitalAddress(hospitalDto.getHospitalAddress());

        return hospital;
    }
}
