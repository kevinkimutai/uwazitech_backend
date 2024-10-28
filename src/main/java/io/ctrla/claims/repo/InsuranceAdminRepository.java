package io.ctrla.claims.repo;

import io.ctrla.claims.entity.InsuranceAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceAdminRepository extends JpaRepository<InsuranceAdmin, Long> {
    InsuranceAdmin findByUserUserId(Long userId);

}
