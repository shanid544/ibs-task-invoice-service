-- V2__add_tax_and_country_to_invoice.sql

ALTER TABLE invoices
ADD COLUMN country_code VARCHAR(10);

ALTER TABLE invoices
ADD COLUMN tax DOUBLE DEFAULT 0.0;
