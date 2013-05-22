SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `phoenix` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `phoenix` ;

-- -----------------------------------------------------
-- Table `phoenix`.`instance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`instance` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`instance` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `host` VARCHAR(45) NOT NULL ,
  `port` INT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`standard_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`standard_role` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`standard_role` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `inheritatedRole` INT NOT NULL DEFAULT -1 ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`role` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `inheritatedRole` INT NOT NULL DEFAULT -1 ,
  `instance_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_role_instance_idx` (`instance_id` ASC) ,
  CONSTRAINT `fk_role_instance`
    FOREIGN KEY (`instance_id` )
    REFERENCES `phoenix`.`instance` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

USE `phoenix` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `phoenix`.`standard_role`
-- -----------------------------------------------------
START TRANSACTION;
USE `phoenix`;
INSERT INTO `phoenix`.`standard_role` (`id`, `name`, `inheritatedRole`) VALUES (1, 'guest', -1);
INSERT INTO `phoenix`.`standard_role` (`id`, `name`, `inheritatedRole`) VALUES (2, 'student', 1);
INSERT INTO `phoenix`.`standard_role` (`id`, `name`, `inheritatedRole`) VALUES (3, 'tutor', 2);
INSERT INTO `phoenix`.`standard_role` (`id`, `name`, `inheritatedRole`) VALUES (4, 'exerciseLeader', 3);
INSERT INTO `phoenix`.`standard_role` (`id`, `name`, `inheritatedRole`) VALUES (5, 'groupLeader', 4);
INSERT INTO `phoenix`.`standard_role` (`id`, `name`, `inheritatedRole`) VALUES (6, 'lectureLeader', 5);
INSERT INTO `phoenix`.`standard_role` (`id`, `name`, `inheritatedRole`) VALUES (7, 'instanceAdmin', 6);
INSERT INTO `phoenix`.`standard_role` (`id`, `name`, `inheritatedRole`) VALUES (8, 'admin', 7);

COMMIT;
