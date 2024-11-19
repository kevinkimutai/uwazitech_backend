package io.ctrla.claims.repo;

import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface HospitalRepository extends JpaRepository<Hospital,Long> {
//    Optional <Hospital> findByHospitalAdminsHospitalId(Long hospitalId);
}
