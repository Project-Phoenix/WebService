SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `phoenix` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `phoenix` ;

-- -----------------------------------------------------
-- Table `phoenix`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`role` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NULL ,
  `inheritatedRole` INT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`user` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `accountName` VARCHAR(45) NOT NULL ,
  `name` VARCHAR(64) NOT NULL ,
  `surname` VARCHAR(64) NOT NULL ,
  `gender` TINYINT(1) NOT NULL ,
  `title` VARCHAR(45) NOT NULL ,
  `matrikel` VARCHAR(45) NULL ,
  `password` VARCHAR(64) NOT NULL ,
  `salt` VARCHAR(64) NOT NULL ,
  `regdate` DATETIME NOT NULL ,
  `isActive` TINYINT(1) NOT NULL ,
  `role_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_user_role1_idx` (`role_id` ASC) ,
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`role_id` )
    REFERENCES `phoenix`.`role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`submission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`submission` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`submission` (
  `id` INT NULL AUTO_INCREMENT ,
  `status` INT NOT NULL ,
  `controlStatus` INT NOT NULL ,
  `user_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_submission_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_submission_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`submission_file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`submission_file` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`submission_file` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content` TEXT NOT NULL ,
  `type` VARCHAR(10) NOT NULL ,
  `submission_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_submission_file_submission1_idx` (`submission_id` ASC) ,
  CONSTRAINT `fk_submission_file_submission1`
    FOREIGN KEY (`submission_id` )
    REFERENCES `phoenix`.`submission` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`permission` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`permission` (
  `permission` VARCHAR(64) NOT NULL ,
  `role_id` INT NOT NULL ,
  PRIMARY KEY (`permission`) ,
  INDEX `fk_permission_role1_idx` (`role_id` ASC) ,
  CONSTRAINT `fk_permission_role1`
    FOREIGN KEY (`role_id` )
    REFERENCES `phoenix`.`role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


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
-- Table `phoenix`.`lecture`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`lecture` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`lecture` (
  `id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `lectur` VARCHAR(45) NOT NULL ,
  `time` DATETIME NOT NULL ,
  `room` VARCHAR(45) NOT NULL ,
  `isActive` TINYINT(1) NOT NULL DEFAULT true ,
  `instance_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_lecture_instance1_idx` (`instance_id` ASC) ,
  CONSTRAINT `fk_lecture_instance1`
    FOREIGN KEY (`instance_id` )
    REFERENCES `phoenix`.`instance` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`group` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`group` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `room` VARCHAR(45) NOT NULL ,
  `turnus` VARCHAR(45) NOT NULL ,
  `lecture_id` INT NOT NULL ,
  `submission_expire_date` DATETIME NULL ,
  `registration_start_date` DATETIME NOT NULL ,
  `registration_end_date` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_group_lecture1_idx` (`lecture_id` ASC) ,
  CONSTRAINT `fk_group_lecture1`
    FOREIGN KEY (`lecture_id` )
    REFERENCES `phoenix`.`lecture` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`exercise_sheet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`exercise_sheet` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`exercise_sheet` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `expirationDate` DATETIME NOT NULL ,
  `release_date` DATETIME NOT NULL ,
  `visible` TINYINT(1) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`task` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`task` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `text` TEXT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`group_has_exercise_sheet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`group_has_exercise_sheet` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`group_has_exercise_sheet` (
  `group_id` INT NOT NULL ,
  `exercise_sheet_id` INT NOT NULL ,
  PRIMARY KEY (`group_id`, `exercise_sheet_id`) ,
  INDEX `fk_group_has_exercise_sheet_exercise_sheet1_idx` (`exercise_sheet_id` ASC) ,
  INDEX `fk_group_has_exercise_sheet_group1_idx` (`group_id` ASC) ,
  CONSTRAINT `fk_group_has_exercise_sheet_group1`
    FOREIGN KEY (`group_id` )
    REFERENCES `phoenix`.`group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_group_has_exercise_sheet_exercise_sheet1`
    FOREIGN KEY (`exercise_sheet_id` )
    REFERENCES `phoenix`.`exercise_sheet` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`exercise_sheet_has_task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`exercise_sheet_has_task` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`exercise_sheet_has_task` (
  `exercise_sheet_id` INT NOT NULL ,
  `task_id` INT NOT NULL ,
  PRIMARY KEY (`exercise_sheet_id`, `task_id`) ,
  INDEX `fk_exercise_sheet_has_task_task1_idx` (`task_id` ASC) ,
  INDEX `fk_exercise_sheet_has_task_exercise_sheet1_idx` (`exercise_sheet_id` ASC) ,
  CONSTRAINT `fk_exercise_sheet_has_task_exercise_sheet1`
    FOREIGN KEY (`exercise_sheet_id` )
    REFERENCES `phoenix`.`exercise_sheet` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_exercise_sheet_has_task_task1`
    FOREIGN KEY (`task_id` )
    REFERENCES `phoenix`.`task` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`submission_for_task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`submission_for_task` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`submission_for_task` (
  `submission_id` INT NOT NULL ,
  `exercise_sheet_has_task_exercise_sheet_id` INT NOT NULL ,
  `exercise_sheet_has_task_task_id` INT NOT NULL ,
  PRIMARY KEY (`submission_id`, `exercise_sheet_has_task_exercise_sheet_id`, `exercise_sheet_has_task_task_id`) ,
  INDEX `fk_submission_has_exercise_sheet_has_task_exercise_sheet_ha_idx` (`exercise_sheet_has_task_exercise_sheet_id` ASC, `exercise_sheet_has_task_task_id` ASC) ,
  INDEX `fk_submission_has_exercise_sheet_has_task_submission1_idx` (`submission_id` ASC) ,
  CONSTRAINT `fk_submission_has_exercise_sheet_has_task_submission1`
    FOREIGN KEY (`submission_id` )
    REFERENCES `phoenix`.`submission` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_submission_has_exercise_sheet_has_task_exercise_sheet_has_1`
    FOREIGN KEY (`exercise_sheet_has_task_exercise_sheet_id` , `exercise_sheet_has_task_task_id` )
    REFERENCES `phoenix`.`exercise_sheet_has_task` (`exercise_sheet_id` , `task_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`instance_admin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`instance_admin` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`instance_admin` (
  `instance_id` INT NOT NULL ,
  `user_id` INT NOT NULL ,
  PRIMARY KEY (`instance_id`, `user_id`) ,
  INDEX `fk_instance_has_user_user1_idx` (`user_id` ASC) ,
  INDEX `fk_instance_has_user_instance1_idx` (`instance_id` ASC) ,
  CONSTRAINT `fk_instance_has_user_instance1`
    FOREIGN KEY (`instance_id` )
    REFERENCES `phoenix`.`instance` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_instance_has_user_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectur`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`lectur` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`lectur` (
  `user_id` INT NOT NULL ,
  `lecture_id` INT NOT NULL ,
  PRIMARY KEY (`user_id`, `lecture_id`) ,
  INDEX `fk_user_has_lecture_lecture1_idx` (`lecture_id` ASC) ,
  INDEX `fk_user_has_lecture_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_user_has_lecture_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_has_lecture_lecture1`
    FOREIGN KEY (`lecture_id` )
    REFERENCES `phoenix`.`lecture` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`group_leader`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`group_leader` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`group_leader` (
  `user_id` INT NOT NULL ,
  `group_id` INT NOT NULL ,
  PRIMARY KEY (`user_id`, `group_id`) ,
  INDEX `fk_user_has_group_group1_idx` (`group_id` ASC) ,
  INDEX `fk_user_has_group_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_user_has_group_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_has_group_group1`
    FOREIGN KEY (`group_id` )
    REFERENCES `phoenix`.`group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`automatic_task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`automatic_task` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`automatic_task` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `backend` VARCHAR(45) NOT NULL ,
  `task_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_automatic_task_task1_idx` (`task_id` ASC) ,
  CONSTRAINT `fk_automatic_task_task1`
    FOREIGN KEY (`task_id` )
    REFERENCES `phoenix`.`task` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`automatic_task_template`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`automatic_task_template` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`automatic_task_template` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `text` TEXT NOT NULL ,
  `automatic_task_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_automatic_task_template_automatic_task1_idx` (`automatic_task_id` ASC) ,
  CONSTRAINT `fk_automatic_task_template_automatic_task1`
    FOREIGN KEY (`automatic_task_id` )
    REFERENCES `phoenix`.`automatic_task` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`automatic_test_test_file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`automatic_test_test_file` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`automatic_test_test_file` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `text` TEXT NOT NULL ,
  `automatic_task_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_automatic_test_test_files_automatic_task1_idx` (`automatic_task_id` ASC) ,
  CONSTRAINT `fk_automatic_test_test_files_automatic_task1`
    FOREIGN KEY (`automatic_task_id` )
    REFERENCES `phoenix`.`automatic_task` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`sample_solution`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`sample_solution` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`sample_solution` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `author` VARCHAR(45) NOT NULL ,
  `lecture_id` INT NOT NULL ,
  `task_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_sample_solution_lecture1_idx` (`lecture_id` ASC) ,
  INDEX `fk_sample_solution_task1_idx` (`task_id` ASC) ,
  CONSTRAINT `fk_sample_solution_lecture1`
    FOREIGN KEY (`lecture_id` )
    REFERENCES `phoenix`.`lecture` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sample_solution_task1`
    FOREIGN KEY (`task_id` )
    REFERENCES `phoenix`.`task` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`sample_solution_file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`sample_solution_file` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`sample_solution_file` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content` TEXT NOT NULL ,
  `type` VARCHAR(10) NOT NULL ,
  `sample_solution_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_sample_solution_file_sample_solution1_idx` (`sample_solution_id` ASC) ,
  CONSTRAINT `fk_sample_solution_file_sample_solution1`
    FOREIGN KEY (`sample_solution_id` )
    REFERENCES `phoenix`.`sample_solution` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`material`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`material` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`material` (
  `id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `category` VARCHAR(45) NULL ,
  `data` LONGBLOB NOT NULL ,
  `visible` TINYINT(1) NOT NULL ,
  `release_date` DATETIME NULL ,
  `lecture_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_material_lecture1_idx` (`lecture_id` ASC) ,
  CONSTRAINT `fk_material_lecture1`
    FOREIGN KEY (`lecture_id` )
    REFERENCES `phoenix`.`lecture` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`user_is_in_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`user_is_in_group` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`user_is_in_group` (
  `user_id` INT NOT NULL ,
  `group_id` INT NOT NULL ,
  PRIMARY KEY (`user_id`, `group_id`) ,
  INDEX `fk_user_has_group_group2_idx` (`group_id` ASC) ,
  INDEX `fk_user_has_group_user2_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_user_has_group_user2`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_has_group_group2`
    FOREIGN KEY (`group_id` )
    REFERENCES `phoenix`.`group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

USE `phoenix` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
