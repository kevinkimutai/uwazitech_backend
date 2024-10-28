package io.ctrla.claims.repo;

import io.ctrla.claims.entity.AdminEntity;
import io.ctrla.claims.entity.HospitalAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
}
