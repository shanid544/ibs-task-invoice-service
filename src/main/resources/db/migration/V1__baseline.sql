-- Marking the initial schema as version 1 without actually modifying tables

CREATE TABLE IF NOT EXISTS invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    billing_id VARCHAR(50) NOT NULL UNIQUE,
    buyer_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    invoice_date DATE NOT NULL,
    payment_due_date DATE NOT NULL,
    currency_code VARCHAR(10) NOT NULL,
    total_invoice_amount DECIMAL(10,2) NOT NULL,
    total_invoice_amount_due DECIMAL(10,2) NOT NULL,
    invoice_language_code VARCHAR(10) NOT NULL,
    payment_method VARCHAR(50),
    payment_reference VARCHAR(100),
    FOREIGN KEY (buyer_id) REFERENCES users(id),
    FOREIGN KEY (supplier_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS billing_lines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id BIGINT NOT NULL,
    item_description VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255),
    phone_number VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);
