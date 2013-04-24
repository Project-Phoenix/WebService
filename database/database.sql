SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `phoenix` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `phoenix` ;

-- -----------------------------------------------------
-- Table `phoenix`.`submission`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `phoenix`.`submission` (
  `id` INT NULL AUTO_INCREMENT ,
  `status` INT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`role`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `phoenix`.`role` (
  `id` INT NOT NULL ,
  `name` VARCHAR(45) NULL ,
  `inheritedRole` INT NOT NULL ,
  PRIMARY KEY (`id`, `inheritedRole`) ,
  INDEX `fk_role_role1_idx` (`inheritedRole` ASC) ,
  CONSTRAINT `fk_role_role1`
    FOREIGN KEY (`inheritedRole` )
    REFERENCES `phoenix`.`role` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `phoenix`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `pw` VARCHAR(64) NOT NULL ,
  `salt` VARCHAR(64) NOT NULL ,
  `regdate` DATETIME NOT NULL ,
  `active` TINYINT(1) NOT NULL ,
  `role_id` INT NOT NULL ,
  PRIMARY KEY (`id`, `role_id`) ,
  INDEX `fk_user_role1_idx` (`role_id` ASC) ,
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`role_id` )
    REFERENCES `phoenix`.`role` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`student`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `phoenix`.`student` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `surname` VARCHAR(45) NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `matrikelnr` INT NOT NULL ,
  `user_id` INT NOT NULL ,
  PRIMARY KEY (`id`, `user_id`) ,
  INDEX `name` (`surname` ASC, `name` ASC) ,
  INDEX `fk_student_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_student_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`task`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `phoenix`.`task` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`task_has_submission`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `phoenix`.`task_has_submission` (
  `task_id` INT NOT NULL ,
  `submission_id` INT NOT NULL ,
  `student_id` INT NOT NULL ,
  PRIMARY KEY (`task_id`, `submission_id`, `student_id`) ,
  INDEX `fk_task_has_submission_submission1_idx` (`submission_id` ASC) ,
  INDEX `fk_task_has_submission_task_idx` (`task_id` ASC) ,
  INDEX `fk_task_has_submission_student1_idx` (`student_id` ASC) ,
  CONSTRAINT `fk_task_has_submission_task`
    FOREIGN KEY (`task_id` )
    REFERENCES `phoenix`.`task` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_has_submission_submission1`
    FOREIGN KEY (`submission_id` )
    REFERENCES `phoenix`.`submission` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_has_submission_student1`
    FOREIGN KEY (`student_id` )
    REFERENCES `phoenix`.`student` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`files`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `phoenix`.`files` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content` TEXT NOT NULL ,
  `type` VARCHAR(10) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`submission_has_files`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `phoenix`.`submission_has_files` (
  `submission_id` INT NOT NULL ,
  `files_id` INT NOT NULL ,
  PRIMARY KEY (`submission_id`, `files_id`) ,
  INDEX `fk_submission_has_files_files1_idx` (`files_id` ASC) ,
  INDEX `fk_submission_has_files_submission1_idx` (`submission_id` ASC) ,
  CONSTRAINT `fk_submission_has_files_submission1`
    FOREIGN KEY (`submission_id` )
    REFERENCES `phoenix`.`submission` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_submission_has_files_files1`
    FOREIGN KEY (`files_id` )
    REFERENCES `phoenix`.`files` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`permission`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `phoenix`.`permission` (
  `permission` VARCHAR(64) NOT NULL ,
  `role_id` INT NOT NULL ,
  PRIMARY KEY (`permission`, `role_id`) ,
  INDEX `fk_permission_role1_idx` (`role_id` ASC) ,
  CONSTRAINT `fk_permission_role1`
    FOREIGN KEY (`role_id` )
    REFERENCES `phoenix`.`role` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `phoenix` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
