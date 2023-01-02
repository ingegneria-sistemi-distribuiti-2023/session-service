-- -----------------------------------------------------
-- Schema sessiondb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sessiondb` DEFAULT CHARACTER SET utf8 ;

-- -----------------------------------------------------
-- Table `sessiondb`.`session`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sessiondb`.`session` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` INT NOT NULL,
    `data` BLOB NOT NULL,
    `start_time` TIMESTAMP NOT NULL,
    `end_time` TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;
