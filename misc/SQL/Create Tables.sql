CREATE TABLE currency (
currency_id INT NOT NULL PRIMARY KEY,
`name` VARCHAR(255),
icon VARCHAR(255)

);



CREATE TABLE sample_date(
date_id INT AUTO_INCREMENT PRIMARY KEY,
`date` DATE,
league_id INT

);

CREATE TABLE league(
league_id INT NOT NULL PRIMARY KEY,
league_name VARCHAR(255)

);

CREATE TABLE currency_Date(
currency_id INT NOT NULL,
date_id INT NOT NULL,
payChaos DECIMAL(30,14),
receiveChaos DECIMAL(30,currencydate14),
PRIMARY KEY(currency_id, date_id),
CONSTRAINT fk_currencyDate_currency FOREIGN KEY(currency_id) REFERENCES	currency(currency_id),
CONSTRAINT fk_currencyDate_sample_date FOREIGN KEY(date_id) REFERENCES sample_date(date_id)


);


