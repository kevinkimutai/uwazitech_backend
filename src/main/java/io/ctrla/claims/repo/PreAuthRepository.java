package io.ctrla.claims.repo;

import io.ctrla.claims.entity.PreAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreAuthRepository extends JpaRepository<PreAuth, Long> {
    List<PreAuth> findPreAuthsByHospitalHospitalId(Long hospitalId);
    List<PreAuth> findPreAuthsByInsuranceInsuranceId(Long insuranceId);
}