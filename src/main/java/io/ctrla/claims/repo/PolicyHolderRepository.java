package io.ctrla.claims.repo;

import io.ctrla.claims.entity.Insurance;
import io.ctrla.claims.entity.InsuranceAdmin;
import io.ctrla.claims.entity.PolicyHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PolicyHolderRepository extends JpaRepository<PolicyHolder,Long> {
    PolicyHolder findPolicyHolderByUserUserId(Long userId);
    Optional <PolicyHolder> findPolicyHolderByPolicyNumber(String policyNumber);
}
