package io.ctrla.claims.repo;

import io.ctrla.claims.entity.HospitalAdmin;
import io.ctrla.claims.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface HospitalAdminRepository extends JpaRepository<HospitalAdmin, Long> {
    @Query(value = "select  h from HospitalAdmin h where h.user.userId = ?1", nativeQuery = true)
    HospitalAdmin findHospitalAdminByUser(Long userId);

}
