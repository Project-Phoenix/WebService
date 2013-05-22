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
-- Table `phoenix`.`default_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`default_role` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`default_role` (
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


-- -----------------------------------------------------
-- Table `phoenix`.`default_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`default_permission` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`default_permission` (
  `node` VARCHAR(64) NOT NULL ,
  `default_role_id` INT NOT NULL ,
  PRIMARY KEY (`node`) ,
  INDEX `fk_default_permission_default_role1_idx` (`default_role_id` ASC) ,
  CONSTRAINT `fk_default_permission_default_role1`
    FOREIGN KEY (`default_role_id` )
    REFERENCES `phoenix`.`default_role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`permission` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`permission` (
  `node` VARCHAR(64) NOT NULL ,
  `role_id` INT NOT NULL ,
  PRIMARY KEY (`node`) ,
  INDEX `fk_permission_role1_idx` (`role_id` ASC) ,
  CONSTRAINT `fk_permission_role1`
    FOREIGN KEY (`role_id` )
    REFERENCES `phoenix`.`role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`user` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `surname` VARCHAR(64) NOT NULL ,
  `name` VARCHAR(64) NOT NULL ,
  `title` VARCHAR(45) NOT NULL ,
  `matrikelNr` VARCHAR(16) NULL ,
  `username` VARCHAR(64) NOT NULL ,
  `password` VARCHAR(64) NOT NULL ,
  `salt` VARCHAR(64) NOT NULL ,
  `email` VARCHAR(64) NOT NULL ,
  `regdate` DATETIME NOT NULL ,
  `isActive` TINYINT(1) NOT NULL DEFAULT 1 ,
  `role_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `name` (`surname` ASC, `name` ASC) ,
  INDEX `userName` (`username` ASC) ,
  INDEX `fk_user_role1_idx` (`role_id` ASC) ,
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`role_id` )
    REFERENCES `phoenix`.`role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

USE `phoenix` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `phoenix`.`default_role`
-- -----------------------------------------------------
START TRANSACTION;
USE `phoenix`;
INSERT INTO `phoenix`.`default_role` (`id`, `name`, `inheritatedRole`) VALUES (1, 'guest', -1);
INSERT INTO `phoenix`.`default_role` (`id`, `name`, `inheritatedRole`) VALUES (2, 'student', 1);
INSERT INTO `phoenix`.`default_role` (`id`, `name`, `inheritatedRole`) VALUES (3, 'tutor', 2);
INSERT INTO `phoenix`.`default_role` (`id`, `name`, `inheritatedRole`) VALUES (4, 'exerciseLeader', 3);
INSERT INTO `phoenix`.`default_role` (`id`, `name`, `inheritatedRole`) VALUES (5, 'groupLeader', 4);
INSERT INTO `phoenix`.`default_role` (`id`, `name`, `inheritatedRole`) VALUES (6, 'lectureLeader', 5);
INSERT INTO `phoenix`.`default_role` (`id`, `name`, `inheritatedRole`) VALUES (7, 'instanceAdmin', 6);
INSERT INTO `phoenix`.`default_role` (`id`, `name`, `inheritatedRole`) VALUES (8, 'admin', 7);

COMMIT;
