DROP DATABASE IF EXISTS searchpp;
CREATE DATABASE searchpp;
USE searchpp;

#list of all register users
CREATE TABLE users (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
    email VARCHAR(255) UNIQUE NOT NULL
);

#list of product id's
CREATE TABLE products (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY
);

#list of site id's / platform
CREATE TABLE sites (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    site_id VARCHAR(255) UNIQUE NOT NULL,
    platform ENUM('amazon', 'ebay') NOT NULL
);

#product id - side id mapping
CREATE TABLE product_to_site (
    product_id INT UNSIGNED,
    side_id INT UNSIGNED,

    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (side_id) REFERENCES sites(id) ON DELETE CASCADE ON UPDATE CASCADE
);
ALTER TABLE product_to_site ADD UNIQUE (product_id, side_id);

#price history for side id's
CREATE TABLE site_price_history (
    site_id INT UNSIGNED NOT NULL,
    price DECIMAL(7,2) NOT NULL,
    date DATETIME NOT NULL,

    FOREIGN KEY (site_id) REFERENCES sites(id) ON DELETE CASCADE ON UPDATE CASCADE
);
ALTER TABLE site_price_history ADD UNIQUE (site_id, date);

#watched products from users
CREATE TABLE usr_product_watch (
    user_id INT UNSIGNED NOT NULL,
    product_id INT UNSIGNED NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE
);
ALTER TABLE usr_product_watch ADD UNIQUE (user_id, product_id);
