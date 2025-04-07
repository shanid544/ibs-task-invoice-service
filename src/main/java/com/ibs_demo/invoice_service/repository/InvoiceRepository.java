package com.ibs_demo.invoice_service.repository;

import com.ibs_demo.invoice_service.entity.Invoice;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.model.InvoiceStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findByBuyer(User buyer, Pageable pageable);
    Page<Invoice> findBySupplier(User supplier, Pageable pageable);
    Optional<Invoice> findByBillingId(String billingId);

    @Query("SELECT i FROM Invoice i WHERE " +
            "(:buyerEmail IS NULL OR i.buyer.email = :buyerEmail) AND " +
            "(:supplierEmail IS NULL OR i.supplier.email = :supplierEmail) AND " +
            "(:status IS NULL OR i.status = :status)")
    Page<Invoice> findFilteredInvoices(
            @Param("buyerEmail") String buyerEmail,
            @Param("supplierEmail") String supplierEmail,
            @Param("status") InvoiceStatus status,
            Pageable pageable
    );

}
