/* Create table customer */
CREATE TABLE `atm`.`customer` (
  `customer_num` INT NOT NULL,
  `full_name` VARCHAR(45) NOT NULL,
  `birth_date` DATE NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(15) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`customer_num`),
  UNIQUE INDEX `customer_id_UNIQUE` (`customer_num` ASC) VISIBLE,
  UNIQUE INDEX `phone_UNIQUE` (`phone` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);

/* Insert data into table customer */
INSERT INTO `atm`.`customer` (`customer_num`, `full_name`, `birth_date`, `address`, `phone`, `email`) VALUES ('1', 'Thanh Pham', '1995-11-11', '3801 W Temple Ave, Pomona, CA 91768', '1-714-724-1111', 'thanhpham@ccp.edu');
INSERT INTO `atm`.`customer` (`customer_num`, `full_name`, `birth_date`, `address`, `phone`, `email`) VALUES ('2', 'Henry Do', '1995-11-12', '3802 W Temple Ave, Pomona, CA 91768', '1-714-724-2222', 'henrydo@ccp.edu');
INSERT INTO `atm`.`customer` (`customer_num`, `full_name`, `birth_date`, `address`, `phone`, `email`) VALUES ('3', 'Jonathan Anz', '1995-11-13', '3803 W Temple Ave, Pomona, CA 91768', '1-714-724-3333', 'jonathan@ccp.edu');
INSERT INTO `atm`.`customer` (`customer_num`, `full_name`, `birth_date`, `address`, `phone`, `email`) VALUES ('4', 'David Torres', '1995-11-14', '3804 W Temple Ave, Pomona, CA 91768', '1-714-724-4444', 'david@cpp.edu');
INSERT INTO `atm`.`customer` (`customer_num`, `full_name`, `birth_date`, `address`, `phone`, `email`) VALUES ('5', 'Michelle Chang', '1995-11-15', '3805 W Temple Ave, Pomona, CA 91768', '1-714-724-5555', 'michelle@cpp.edu');
INSERT INTO `atm`.`customer` (`customer_num`, `full_name`, `birth_date`, `address`, `phone`, `email`) VALUES ('6', 'Connor McCarry', '1995-11-16', '3806 W Temple Ave, Pomona, CA 91768', '1-714-724-6666', 'connor@cpp.edu');

/* Create table checking account*/
CREATE TABLE `atm`.`checking_account` (
  `acc_id` INT NOT NULL,
  `balance` DECIMAL(15,2) NOT NULL,
  `cus_num` INT NOT NULL,
  PRIMARY KEY (`acc_id`),
  UNIQUE INDEX `acc_id_UNIQUE` (`acc_id` ASC) VISIBLE,
  FOREIGN KEY (`cus_num`)
        REFERENCES customer(`customer_num`));

/* Insert data into table checking account */
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('101', '1000', '1');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('102', '2000', '2');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('103', '3000', '3');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('104', '4000', '4');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('105', '5000', '5');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('106', '6000', '6');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('201', '0', '1');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('202', '0', '2');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('203', '0', '3');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('204', '0', '4');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('205', '0', '5');
INSERT INTO `atm`.`checking_account` (`acc_id`, `balance`, `cus_num`) VALUES ('206', '0', '6');

/* Create table saving account */
CREATE TABLE `atm`.`saving_account` (
  `acc_id` INT NOT NULL,
  `balance` DECIMAL(15,2) NOT NULL,
  `interest_rate` DECIMAL(3,2) NOT NULL,
  `cus_num` INT NOT NULL,
  PRIMARY KEY (`acc_id`),
  UNIQUE INDEX `acc_id_UNIQUE` (`acc_id` ASC) VISIBLE,
FOREIGN KEY (`cus_num`)
        REFERENCES customer(`customer_num`));

/* Insert data into table saving account */
INSERT INTO `atm`.`saving_account` (`acc_id`, `balance`, `interest_rate`, `cus_num`) VALUES ('301', '1000', '4', '1');
INSERT INTO `atm`.`saving_account` (`acc_id`, `balance`, `interest_rate`, `cus_num`) VALUES ('302', '2000', '5', '2');
INSERT INTO `atm`.`saving_account` (`acc_id`, `balance`, `interest_rate`, `cus_num`) VALUES ('303', '3000', '6', '3');
INSERT INTO `atm`.`saving_account` (`acc_id`, `balance`, `interest_rate`, `cus_num`) VALUES ('304', '4000', '7', '4');
INSERT INTO `atm`.`saving_account` (`acc_id`, `balance`, `interest_rate`, `cus_num`) VALUES ('305', '5000', '8', '5');
INSERT INTO `atm`.`saving_account` (`acc_id`, `balance`, `interest_rate`, `cus_num`) VALUES ('306', '6000', '9', '6');

/* Create table debit card */
CREATE TABLE `atm`.`debit_card` (
  `card_num` INT NOT NULL,
  `pin_num` INT NOT NULL,
  `expiration_date` DATE NOT NULL,
  `cvv` INT NOT NULL,
  `acc_id` INT NOT NULL,
  `cus_num` INT NOT NULL,
  PRIMARY KEY (`card_num`),
  UNIQUE INDEX `card_num_UNIQUE` (`card_num` ASC) VISIBLE,
FOREIGN KEY (`acc_id`)
        REFERENCES checking_account(`acc_id`),
FOREIGN KEY (`cus_num`)
        REFERENCES customer(`customer_num`));

/* Insert data into table debit card */
INSERT INTO `atm`.`debit_card` (`card_num`, `pin_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('401', '701', '2025-11-15', '601', '101', '1');
INSERT INTO `atm`.`debit_card` (`card_num`, `pin_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('402', '702', '2025-11-15', '602', '102', '2');
INSERT INTO `atm`.`debit_card` (`card_num`, `pin_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('403', '703', '2025-11-15', '603', '103', '3');
INSERT INTO `atm`.`debit_card` (`card_num`, `pin_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('404', '704', '2025-11-15', '604', '104', '4');
INSERT INTO `atm`.`debit_card` (`card_num`, `pin_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('405', '705', '2025-11-15', '605', '105', '5');
INSERT INTO `atm`.`debit_card` (`card_num`, `pin_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('406', '706', '2025-11-15', '606', '106', '6');

/* Create table credit card */
CREATE TABLE `atm`.`credit_card` (
  `card_num` INT NOT NULL,
  `expiration_date` DATE NOT NULL,
  `cvv` INT NOT NULL,
  `acc_id` INT NOT NULL,
  `cus_num` INT NOT NULL,
  PRIMARY KEY (`card_num`),
FOREIGN KEY (`acc_id`)
        REFERENCES checking_account(`acc_id`),
FOREIGN KEY (`cus_num`)
        REFERENCES customer(`customer_num`));

/* Insert data into table credit card */
INSERT INTO `atm`.`credit_card` (`card_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('501', '2025-11-15', '801', '201', '1');
INSERT INTO `atm`.`credit_card` (`card_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('502', '2025-11-15', '802', '202', '2');
INSERT INTO `atm`.`credit_card` (`card_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('503', '2025-11-15', '803', '203', '3');
INSERT INTO `atm`.`credit_card` (`card_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('504', '2025-11-15', '804', '204', '4');
INSERT INTO `atm`.`credit_card` (`card_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('505', '2025-11-15', '805', '205', '5');
INSERT INTO `atm`.`credit_card` (`card_num`, `expiration_date`, `cvv`, `acc_id`, `cus_num`) VALUES ('506', '2025-11-15', '806', '206', '6');

/* Create table transaction */
CREATE TABLE `atm`.`transaction` (
  `trans_num` INT NOT NULL AUTO_INCREMENT,
  `trans_date` DATE NOT NULL,
  `trans_type` VARCHAR(15) NOT NULL,
  `amount` VARCHAR(15) NOT NULL,
  `history` DECIMAL(15,2) NOT NULL,
  `checking_id` INT,
  `saving_id` INT,
  PRIMARY KEY (`trans_num`),
FOREIGN KEY (`checking_id`)
        REFERENCES checking_account(`acc_id`),
FOREIGN KEY (`saving_id`)
        REFERENCES saving_account(`acc_id`));

/* Insert data into table  transaction */
INSERT INTO `atm`.`transaction` (`trans_num`, `trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES ('1', '2023-11-15', 'Deposit', '+1000.00', '1000', '101');
INSERT INTO `atm`.`transaction` (`trans_num`, `trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES ('2', '2023-11-15', 'Deposit', '+2000.00', '2000', '102');
INSERT INTO `atm`.`transaction` (`trans_num`, `trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES ('3', '2023-11-15', 'Deposit', '+3000.00', '3000', '103');
INSERT INTO `atm`.`transaction` (`trans_num`, `trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES ('4', '2023-11-15', 'Deposit', '+4000.00', '4000', '104');
INSERT INTO `atm`.`transaction` (`trans_num`, `trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES ('5', '2023-11-15', 'Deposit', '+5000.00', '5000', '105');
INSERT INTO `atm`.`transaction` (`trans_num`, `trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES ('6', '2023-11-15', 'Deposit', '+5000.00', '6000', '106');
INSERT INTO `atm`.`transaction` (`trans_num`, `trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES ('7', '2023-11-15', 'Deposit', '+1000.00', '2000', '101');
INSERT INTO `atm`.`transaction` (`trans_num`, `trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES ('8', '2023-11-15', 'Withdraw', '-300.00', '1700', '101');
INSERT INTO `atm`.`transaction` (`trans_num`, `trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES ('9', '2023-11-15', 'Withdraw', '-100.00', '1600', '101');


/* Create table bill */
CREATE TABLE `atm`.`bill` (
  `bill_id` INT NOT NULL,
  `bill_type` VARCHAR(15) NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`bill_id`),
  UNIQUE INDEX `bill_id_UNIQUE` (`bill_id` ASC) VISIBLE);

/* Insert data into table bill */
INSERT INTO `atm`.`bill` (`bill_id`, `bill_type`, `quantity`) VALUES ('1', '$1', '500');
INSERT INTO `atm`.`bill` (`bill_id`, `bill_type`, `quantity`) VALUES ('2', '$5', '500');
INSERT INTO `atm`.`bill` (`bill_id`, `bill_type`, `quantity`) VALUES ('3', '$10', '500');
INSERT INTO `atm`.`bill` (`bill_id`, `bill_type`, `quantity`) VALUES ('4', '$20', '500');
INSERT INTO `atm`.`bill` (`bill_id`, `bill_type`, `quantity`) VALUES ('5', '$100', '500');
