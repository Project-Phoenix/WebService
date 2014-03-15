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
  `title` VARCHAR(255) NULL,
  `description` LONGTEXT NULL,
  `isAutomaticTest` TINYINT(1) NULL DEFAULT FALSE,
  `backend` VARCHAR(45) NULL,
  `disallowedContent` LONGTEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `title` (`title` ASC),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskSheet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskSheet` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NULL,
  `creationDate` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `taskSheetKey` (`creationDate` ASC),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`attachment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`attachment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `file` LONGBLOB NULL,
  `creationDate` DATETIME NULL,
  `name` VARCHAR(255) NULL,
  `type` VARCHAR(45) NULL,
  `sha1` VARCHAR(40) NULL,
  PRIMARY KEY (`id`),
  INDEX `attachmentKey` (`creationDate` ASC, `name` ASC, `type` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`text`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`text` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` LONGTEXT NULL,
  `creationDate` DATETIME NULL,
  `title` VARCHAR(255) NULL,
  `type` VARCHAR(45) NULL,
  `sha1` VARCHAR(40) NULL,
  PRIMARY KEY (`id`),
  INDEX `textKey` (`creationDate` ASC, `title` ASC, `type` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lecture`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lecture` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `lectureKey` (`title` ASC),
  UNIQUE INDEX `lectureUniqueKey` (`title` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectureGroup`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lectureGroup` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `maxMember` INT NULL,
  `submissionDeadlineTime` TIME NULL,
  `submissionDeadlineWeekday` TINYINT NULL,
  `lecture` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_lectureGroup_lecture1_idx` (`lecture` ASC),
  INDEX `lectureGroupKey` (`name` ASC),
  UNIQUE INDEX `lectureGroupIndex` (`name` ASC, `lecture` ASC),
  CONSTRAINT `fk_lectureGroup_lecture1`
    FOREIGN KEY (`lecture`)
    REFERENCES `phoenix`.`lecture` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
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
  `startDate` DATE NULL,
  `endDate` DATE NULL,
  PRIMARY KEY (`id`),
  INDEX `detailsKey` (`room` ASC, `weekday` ASC, `startTime` ASC, `endTime` ASC, `interval` ASC, `startDate` ASC, `endDate` ASC))
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
  INDEX `submissionKey` (`date` ASC, `status` ASC, `statusText`(50) ASC),
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


-- -----------------------------------------------------
-- Table `phoenix`.`lectureGroupTaskSheet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`lectureGroupTaskSheet` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `defaultDeadline` DATETIME NULL,
  `defaultReleaseDate` DATETIME NULL,
  `taskSheet` INT NULL,
  `lectureGroup` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_lectureGroupTaskSheet_taskSheet2_idx` (`taskSheet` ASC),
  INDEX `fk_lectureGroupTaskSheet_lectureGroup1_idx` (`lectureGroup` ASC),
  UNIQUE INDEX `lectureGroupTaskSheetKey` (`taskSheet` ASC, `lectureGroup` ASC),
  CONSTRAINT `fk_lectureGroupTaskSheet_taskSheet2`
    FOREIGN KEY (`taskSheet`)
    REFERENCES `phoenix`.`taskSheet` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lectureGroupTaskSheet_lectureGroup1`
    FOREIGN KEY (`lectureGroup`)
    REFERENCES `phoenix`.`lectureGroup` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskSubmissionDates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskSubmissionDates` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `deadline` DATETIME NULL,
  `releasedate` DATETIME NULL,
  `lectureGroupTaskSheet` INT NULL,
  `task` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_taskSubmissionDates_lectureGroupTaskSheet1_idx` (`lectureGroupTaskSheet` ASC),
  INDEX `fk_taskSubmissionDates_task1_idx` (`task` ASC),
  CONSTRAINT `fk_taskSubmissionDates_lectureGroupTaskSheet1`
    FOREIGN KEY (`lectureGroupTaskSheet`)
    REFERENCES `phoenix`.`lectureGroupTaskSheet` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_taskSubmissionDates_task1`
    FOREIGN KEY (`task`)
    REFERENCES `phoenix`.`task` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskTest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`taskTest` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `timeout` INT NULL,
  `task` INT NOT NULL,
  `text` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_test_task1_idx` (`task` ASC),
  INDEX `fk_test_text1_idx` (`text` ASC),
  CONSTRAINT `fk_test_task1`
    FOREIGN KEY (`task`)
    REFERENCES `phoenix`.`task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_test_text1`
    FOREIGN KEY (`text`)
    REFERENCES `phoenix`.`text` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`debugLog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phoenix`.`debugLog` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `log` LONGTEXT NOT NULL,
  `date` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
