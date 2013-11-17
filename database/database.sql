SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `phoenix` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `phoenix` ;

-- -----------------------------------------------------
-- Table `phoenix`.`task`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NULL,
  `description` LONGTEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `title` (`title` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskSheet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskSheet` (
  `id` INT NOT NULL,
  `creationDate` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`attachment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`attachment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `file` LONGBLOB NULL,
  `creationDate` DATETIME NULL,
  `name` VARCHAR(45) NULL,
  `type` VARCHAR(45) NULL,
  `sha1` VARCHAR(40) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`text`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`text` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` LONGTEXT NULL,
  `creationDate` DATETIME NULL,
  `name` VARCHAR(45) NULL,
  `type` VARCHAR(45) NULL,
  `sha1` VARCHAR(40) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectureGroup`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lectureGroup` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `maxMember` INT NULL,
  `submissionEndDate` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lecture`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lecture` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `room` VARCHAR(45) NULL,
  `weekday` INT NULL,
  `startTime` TIME NULL,
  `endTime` TIME NULL,
  `interval` VARCHAR(45) NULL,
  `startDate` DATETIME NULL,
  `endDate` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`automaticTask`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`automaticTask` (
  `id` INT NOT NULL,
  `backend` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_automaticTask_task1`
    FOREIGN KEY (`id`)
    REFERENCES `phoenix`.`task` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`test`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`test` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `task` INT NOT NULL,
  `test` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_test_text1_idx` (`task` ASC),
  INDEX `fk_test_text2_idx` (`test` ASC),
  CONSTRAINT `fk_test_text1`
    FOREIGN KEY (`task`)
    REFERENCES `phoenix`.`text` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_test_text2`
    FOREIGN KEY (`test`)
    REFERENCES `phoenix`.`text` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`automaticTaskTests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`automaticTaskTests` (
  `automaticTask_id` INT NOT NULL,
  `test_id` INT NOT NULL,
  PRIMARY KEY (`automaticTask_id`, `test_id`),
  INDEX `fk_automaticTaskTests_test1_idx` (`test_id` ASC),
  INDEX `fk_automaticTaskTests_automaticTask1_idx` (`automaticTask_id` ASC),
  CONSTRAINT `fk_automaticTaskTests_automaticTask1`
    FOREIGN KEY (`automaticTask_id`)
    REFERENCES `phoenix`.`automaticTask` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_automaticTaskTests_test1`
    FOREIGN KEY (`test_id`)
    REFERENCES `phoenix`.`test` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskSheetTasks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskSheetTasks` (
  `taskSheet_id` INT NOT NULL,
  `task_id` INT NOT NULL,
  PRIMARY KEY (`taskSheet_id`, `task_id`),
  INDEX `fk_taskSheetTasks_task1_idx` (`task_id` ASC),
  INDEX `fk_taskSheetTasks_taskSheet1_idx` (`taskSheet_id` ASC),
  CONSTRAINT `fk_taskSheetTasks_taskSheet1`
    FOREIGN KEY (`taskSheet_id`)
    REFERENCES `phoenix`.`taskSheet` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_taskSheetTasks_task1`
    FOREIGN KEY (`task_id`)
    REFERENCES `phoenix`.`task` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectureGroupTaskSheet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lectureGroupTaskSheet` (
  `group` INT NOT NULL,
  `taskSheet` INT NOT NULL,
  `defaultDeadline` DATETIME NULL,
  `defaultReleaseDate` DATETIME NULL,
  PRIMARY KEY (`group`, `taskSheet`),
  INDEX `fk_lectureGroupTaskSheet_taskSheet1_idx` (`taskSheet` ASC),
  INDEX `fk_lectureGroupTaskSheet_group1_idx` (`group` ASC),
  CONSTRAINT `fk_lectureGroupTaskSheet_group1`
    FOREIGN KEY (`group`)
    REFERENCES `phoenix`.`lectureGroup` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lectureGroupTaskSheet_taskSheet1`
    FOREIGN KEY (`taskSheet`)
    REFERENCES `phoenix`.`taskSheet` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectureGroups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lectureGroups` (
  `lecture_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  PRIMARY KEY (`lecture_id`, `group_id`),
  INDEX `fk_lectureGroups_group1_idx` (`group_id` ASC),
  INDEX `fk_lectureGroups_lecture1_idx` (`lecture_id` ASC),
  CONSTRAINT `fk_lecture_has_group_lecture1`
    FOREIGN KEY (`lecture_id`)
    REFERENCES `phoenix`.`lecture` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lecture_has_group_group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `phoenix`.`lectureGroup` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskAttachments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskAttachments` (
  `task_id` INT NOT NULL,
  `attachment_id` INT NOT NULL,
  PRIMARY KEY (`task_id`, `attachment_id`),
  INDEX `fk_taskAttachments_attachment1_idx` (`attachment_id` ASC),
  INDEX `fk_taskAttachments_task1_idx` (`task_id` ASC),
  CONSTRAINT `fk_taskAttachments_task1`
    FOREIGN KEY (`task_id`)
    REFERENCES `phoenix`.`task` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_taskAttachments_attachment1`
    FOREIGN KEY (`attachment_id`)
    REFERENCES `phoenix`.`attachment` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskPattern`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskPattern` (
  `task_id` INT NOT NULL,
  `text_id` INT NOT NULL,
  PRIMARY KEY (`task_id`, `text_id`),
  INDEX `fk_taskPattern_text1_idx` (`text_id` ASC),
  INDEX `fk_taskPatternt_task1_idx` (`task_id` ASC),
  CONSTRAINT `fk_taskPattern_task1`
    FOREIGN KEY (`task_id`)
    REFERENCES `phoenix`.`task` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_taskPattern_text1`
    FOREIGN KEY (`text_id`)
    REFERENCES `phoenix`.`text` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectureGroupDetails`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lectureGroupDetails` (
  `group_id` INT NOT NULL,
  `additionalInfo_id` INT NOT NULL,
  PRIMARY KEY (`group_id`, `additionalInfo_id`),
  INDEX `fk_lectureGroupDetails_additionalInfo1_idx` (`additionalInfo_id` ASC),
  INDEX `fk_lectureGroupDetails_group1_idx` (`group_id` ASC),
  CONSTRAINT `fk_lectureGroupDetails_group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `phoenix`.`lectureGroup` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lectureGroupDetails_additionalInfo1`
    FOREIGN KEY (`additionalInfo_id`)
    REFERENCES `phoenix`.`details` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectureDetails`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lectureDetails` (
  `lecture_id` INT NOT NULL,
  `additionalInfo_id` INT NOT NULL,
  PRIMARY KEY (`lecture_id`, `additionalInfo_id`),
  INDEX `fk_lectureDetails_additionalInfo1_idx` (`additionalInfo_id` ASC),
  INDEX `fk_lectureDetails_lecture1_idx` (`lecture_id` ASC),
  CONSTRAINT `fk_lectureDetails_lecture1`
    FOREIGN KEY (`lecture_id`)
    REFERENCES `phoenix`.`lecture` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lectureDetails_additionalInfo1`
    FOREIGN KEY (`additionalInfo_id`)
    REFERENCES `phoenix`.`details` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectureGroupTaskSheetDates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lectureGroupTaskSheetDates` (
  `groupTaskSheet_group` INT NOT NULL,
  `groupTaskSheet_taskSheet` INT NOT NULL,
  `task_id` INT NOT NULL,
  `deadline` DATETIME NULL,
  `releaseDate` DATETIME NULL,
  PRIMARY KEY (`groupTaskSheet_group`, `groupTaskSheet_taskSheet`, `task_id`),
  INDEX `fk_lectureGroupTaskSheetDates_task1_idx` (`task_id` ASC),
  INDEX `fk_lectureGroupTaskSheetDates_groupTaskSheet1_idx` (`groupTaskSheet_group` ASC, `groupTaskSheet_taskSheet` ASC),
  CONSTRAINT `fk_lectureGroupTaskSheetDates_groupTaskSheet1`
    FOREIGN KEY (`groupTaskSheet_group` , `groupTaskSheet_taskSheet`)
    REFERENCES `phoenix`.`lectureGroupTaskSheet` (`group` , `taskSheet`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lectureGroupTaskSheetDates_task1`
    FOREIGN KEY (`task_id`)
    REFERENCES `phoenix`.`task` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskSubmission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskSubmission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME NOT NULL,
  `task` INT NOT NULL,
  `status` INT NULL,
  `statusText` LONGTEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_taskSubmission_task1_idx` (`task` ASC),
  CONSTRAINT `fk_taskSubmission_task1`
    FOREIGN KEY (`task`)
    REFERENCES `phoenix`.`task` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskSubmissionText`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskSubmissionText` (
  `taskSubmission_id` INT NOT NULL,
  `text_id` INT NOT NULL,
  PRIMARY KEY (`taskSubmission_id`, `text_id`),
  INDEX `fk_taskSubmission_has_text_text1_idx` (`text_id` ASC),
  INDEX `fk_taskSubmission_has_text_taskSubmission1_idx` (`taskSubmission_id` ASC),
  CONSTRAINT `fk_taskSubmission_has_text_taskSubmission1`
    FOREIGN KEY (`taskSubmission_id`)
    REFERENCES `phoenix`.`taskSubmission` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_taskSubmission_has_text_text1`
    FOREIGN KEY (`text_id`)
    REFERENCES `phoenix`.`text` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskSubmissionAttachment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskSubmissionAttachment` (
  `taskSubmission_id` INT NOT NULL,
  `attachment_id` INT NOT NULL,
  PRIMARY KEY (`taskSubmission_id`, `attachment_id`),
  INDEX `fk_taskSubmission_has_attachment_attachment1_idx` (`attachment_id` ASC),
  INDEX `fk_taskSubmission_has_attachment_taskSubmission1_idx` (`taskSubmission_id` ASC),
  CONSTRAINT `fk_taskSubmission_has_attachment_taskSubmission1`
    FOREIGN KEY (`taskSubmission_id`)
    REFERENCES `phoenix`.`taskSubmission` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_taskSubmission_has_attachment_attachment1`
    FOREIGN KEY (`attachment_id`)
    REFERENCES `phoenix`.`attachment` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
