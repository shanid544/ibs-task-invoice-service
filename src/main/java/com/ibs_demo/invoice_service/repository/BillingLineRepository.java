package com.ibs_demo.invoice_service.repository;

import com.ibs_demo.invoice_service.entity.BillingLine;
import com.ibs_demo.invoice_service.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingLineRepository extends JpaRepository<BillingLine, Long> {
    List<BillingLine> findByInvoice(Invoice invoice);
}
