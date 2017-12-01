DROP DATABASE IF EXISTS searchpp;
CREATE DATABASE searchpp;
USE searchpp;

CREATE TABLE usr (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
    email VARCHAR(255) NOT NULL
);

CREATE TABLE product (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE product_to_site (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    product_id INT UNSIGNED
);

CREATE TABLE product_site (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    site_id VARCHAR(255) NOT NULL,
    platform ENUM('amazon', 'ebay') NOT NULL
);

CREATE TABLE product_site_price_history (
    product_site_id INT NOT NULL,
    price DECIMAL(7,2),
    date DATETIME
);

CREATE TABLE usr_product_watch (
    usr_id INT UNSIGNED NOT NULL ,
    product_id INT UNSIGNED NOT NULL
);
