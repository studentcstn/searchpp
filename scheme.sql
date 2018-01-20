DROP DATABASE IF EXISTS searchpp;
CREATE DATABASE searchpp;
ALTER DATABASE searchpp CHARACTER SET utf8 COLLATE utf8_general_ci;
USE searchpp;

/* list of all register users */
CREATE TABLE users (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    access_token VARCHAR(255),
    refresh_token VARCHAR(255)
);
ALTER TABLE users ADD UNIQUE (token);

/* list of product id's */
CREATE TABLE products (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY
);

/* product id - side id mapping */
CREATE TABLE product_to_site (
    product_id INT UNSIGNED,
    site_id VARCHAR(255) UNIQUE NOT NULL,
    platform ENUM('amazon', 'ebay') NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE
);
ALTER TABLE product_to_site ADD UNIQUE (product_id, site_id);

/* price history for side id's */
CREATE TABLE site_price_history (
    product_id int unsigned NOT NULL,
    price DECIMAL(7,2) NOT NULL,
    date DATETIME NOT NULL,

    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE
);
ALTER TABLE site_price_history ADD UNIQUE (product_id, date);

/* watched products from users */
CREATE TABLE usr_product_watch (
    user_id INT UNSIGNED NOT NULL,
    product_id INT UNSIGNED NOT NULL,
    event_id VARCHAR(255) NULL,
    date_from DATE NOT NULL,
    date_to DATE NOT NULL ,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE
);
ALTER TABLE usr_product_watch ADD UNIQUE (user_id, product_id);
