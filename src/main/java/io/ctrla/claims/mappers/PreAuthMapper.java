package io.ctrla.claims.mappers;

import io.ctrla.claims.dto.preauth.PreAuthDto;
import io.ctrla.claims.entity.PreAuth;
import org.springframework.stereotype.Service;

@Service
public class PreAuthMapper {

    public PreAuth toPreAuth(PreAuthDto preAuthDto){
        PreAuth preAuth = new PreAuth();
        preAuth.setHospital(preAuthDto.getHospital());
        preAuth.setInsurance(preAuthDto.getInsurance());
        preAuth.setPolicyHolder(preAuthDto.getPolicyHolder());

        return preAuth;

    }
}
