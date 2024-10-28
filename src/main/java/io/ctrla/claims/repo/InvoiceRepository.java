package io.ctrla.claims.repo;

import io.ctrla.claims.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {


}
