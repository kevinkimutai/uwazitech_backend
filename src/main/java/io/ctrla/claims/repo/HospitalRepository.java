package io.ctrla.claims.repo;

import io.ctrla.claims.entity.Hospital;
import io.ctrla.claims.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HospitalRepository extends JpaRepository<Hospital,Long> {}
