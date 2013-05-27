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
-- Table `phoenix`.`defaultRole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`defaultRole` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`defaultRole` (
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
-- Table `phoenix`.`defaultPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`defaultPermission` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`defaultPermission` (
  `node` VARCHAR(64) NOT NULL ,
  `default_role_id` INT NOT NULL ,
  PRIMARY KEY (`node`) ,
  INDEX `fk_default_permission_default_role1_idx` (`default_role_id` ASC) ,
  CONSTRAINT `fk_default_permission_default_role1`
    FOREIGN KEY (`default_role_id` )
    REFERENCES `phoenix`.`defaultRole` (`id` )
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


-- -----------------------------------------------------
-- Table `phoenix`.`message`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`message` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`message` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(80) NOT NULL ,
  `sentDate` DATETIME NOT NULL ,
  `text` TEXT NOT NULL ,
  `sender` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_message_user1_idx` (`sender` ASC) ,
  CONSTRAINT `fk_message_user1`
    FOREIGN KEY (`sender` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`messageReceiver`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`messageReceiver` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`messageReceiver` (
  `message` INT UNSIGNED NOT NULL ,
  `receiver` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`message`, `receiver`) ,
  INDEX `fk_message_receiver_user_idx` (`receiver` ASC) ,
  INDEX `fk_message_receiver_message_idx` (`message` ASC) ,
  CONSTRAINT `fk_message_receiver_message`
    FOREIGN KEY (`message` )
    REFERENCES `phoenix`.`message` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_message_receiver_user`
    FOREIGN KEY (`receiver` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`instanceAdmin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`instanceAdmin` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`instanceAdmin` (
  `user_id` INT UNSIGNED NOT NULL ,
  `instance_id` INT NOT NULL ,
  PRIMARY KEY (`user_id`, `instance_id`) ,
  INDEX `fk_instance_admin_instance1_idx` (`instance_id` ASC) ,
  INDEX `fk_instance_admin_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_instance_admin_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_instance_admin_instance1`
    FOREIGN KEY (`instance_id` )
    REFERENCES `phoenix`.`instance` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lecture`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`lecture` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`lecture` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(80) NOT NULL ,
  `startTime` DATETIME NOT NULL ,
  `endTime` DATETIME NOT NULL ,
  `room` VARCHAR(45) NOT NULL ,
  `isActive` TINYINT(1) NOT NULL DEFAULT 1 ,
  `instance_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `name` (`name` ASC) ,
  INDEX `fk_lecture_instance1_idx` (`instance_id` ASC) ,
  CONSTRAINT `fk_lecture_instance1`
    FOREIGN KEY (`instance_id` )
    REFERENCES `phoenix`.`instance` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectureLeader`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`lectureLeader` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`lectureLeader` (
  `user_id` INT UNSIGNED NOT NULL ,
  `lecture_id` INT NOT NULL ,
  PRIMARY KEY (`user_id`, `lecture_id`) ,
  INDEX `fk_lecture_leader_lecture1_idx` (`lecture_id` ASC) ,
  INDEX `fk_lecture_leader_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_lecture_leader_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lecture_leader_lecture1`
    FOREIGN KEY (`lecture_id` )
    REFERENCES `phoenix`.`lecture` (`id` )
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
  `submission_expire_date` DATETIME NULL ,
  `registration_start_date` DATETIME NOT NULL ,
  `registration_end_date` DATETIME NOT NULL ,
  `lecture` INT NOT NULL ,
  `exerciseLeader` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_group_lecture1_idx` (`lecture` ASC) ,
  INDEX `fk_group_user1_idx` (`exerciseLeader` ASC) ,
  CONSTRAINT `fk_group_lecture1`
    FOREIGN KEY (`lecture` )
    REFERENCES `phoenix`.`lecture` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_group_user1`
    FOREIGN KEY (`exerciseLeader` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`groupLeader`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`groupLeader` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`groupLeader` (
  `user_id` INT UNSIGNED NOT NULL ,
  `lecture_id` INT NOT NULL ,
  PRIMARY KEY (`user_id`, `lecture_id`) ,
  INDEX `fk_group_leader_lecture1_idx` (`lecture_id` ASC) ,
  INDEX `fk_group_leader_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_group_leader_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_group_leader_lecture1`
    FOREIGN KEY (`lecture_id` )
    REFERENCES `phoenix`.`lecture` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`groupMember`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`groupMember` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`groupMember` (
  `user_id` INT UNSIGNED NOT NULL ,
  `group_id` INT NOT NULL ,
  PRIMARY KEY (`user_id`, `group_id`) ,
  INDEX `fk_groupMember_group1_idx` (`group_id` ASC) ,
  INDEX `fk_groupMember_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_groupMember_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_groupMember_group1`
    FOREIGN KEY (`group_id` )
    REFERENCES `phoenix`.`group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`news`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`news` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`news` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(64) NOT NULL ,
  `text` TEXT NOT NULL ,
  `creationDate` DATETIME NOT NULL ,
  `releaseDate` DATETIME NULL ,
  `lecture_id` INT NOT NULL ,
  `author` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_news_lecture1_idx` (`lecture_id` ASC) ,
  INDEX `fk_news_user1_idx` (`author` ASC) ,
  CONSTRAINT `fk_news_lecture1`
    FOREIGN KEY (`lecture_id` )
    REFERENCES `phoenix`.`lecture` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_news_user1`
    FOREIGN KEY (`author` )
    REFERENCES `phoenix`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`taskPool`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`taskPool` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`taskPool` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` TEXT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`tag` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`tag` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `tag` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `tag` (`tag` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`task_has_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`task_has_tag` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`task_has_tag` (
  `task_id` INT UNSIGNED NOT NULL ,
  `tag_id` INT NOT NULL ,
  PRIMARY KEY (`task_id`, `tag_id`) ,
  INDEX `fk_task_has_tag_tag1_idx` (`tag_id` ASC) ,
  INDEX `fk_task_has_tag_task1_idx` (`task_id` ASC) ,
  CONSTRAINT `fk_task_has_tag_task1`
    FOREIGN KEY (`task_id` )
    REFERENCES `phoenix`.`taskPool` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_task_has_tag_tag1`
    FOREIGN KEY (`tag_id` )
    REFERENCES `phoenix`.`tag` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`exerciseSheetPool`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`exerciseSheetPool` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`exerciseSheetPool` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`exerciseSheet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`exerciseSheet` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`exerciseSheet` (
  `group_id` INT NOT NULL ,
  `exercise_sheet_id` INT NOT NULL ,
  `releaseDate` DATETIME NULL ,
  `expirationDate` DATETIME NULL ,
  `visible` TINYINT(1) NULL DEFAULT 1 ,
  PRIMARY KEY (`exercise_sheet_id`, `group_id`) ,
  INDEX `fk_group_has_exercise_sheet_exercise_sheet1_idx` (`exercise_sheet_id` ASC) ,
  INDEX `fk_group_has_exercise_sheet_group1_idx` (`group_id` ASC) ,
  CONSTRAINT `fk_group_has_exercise_sheet_group1`
    FOREIGN KEY (`group_id` )
    REFERENCES `phoenix`.`group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_group_has_exercise_sheet_exercise_sheet1`
    FOREIGN KEY (`exercise_sheet_id` )
    REFERENCES `phoenix`.`exerciseSheetPool` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`task` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`task` (
  `exercise_sheet_pool_id` INT NOT NULL ,
  `task_id` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`exercise_sheet_pool_id`, `task_id`) ,
  INDEX `fk_exercise_sheet_pool_has_task_task1_idx` (`task_id` ASC) ,
  INDEX `fk_exercise_sheet_pool_has_task_exercise_sheet_pool1_idx` (`exercise_sheet_pool_id` ASC) ,
  CONSTRAINT `fk_exercise_sheet_pool_has_task_exercise_sheet_pool1`
    FOREIGN KEY (`exercise_sheet_pool_id` )
    REFERENCES `phoenix`.`exerciseSheetPool` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_exercise_sheet_pool_has_task_task1`
    FOREIGN KEY (`task_id` )
    REFERENCES `phoenix`.`taskPool` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`material`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`material` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`material` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `category` VARCHAR(45) NULL ,
  `data` LONGBLOB NOT NULL ,
  `visible` TINYINT(1) NOT NULL DEFAULT 1 ,
  `releaseDate` DATETIME NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`lectureMaterial`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`lectureMaterial` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`lectureMaterial` (
  `lecture_id` INT NOT NULL ,
  `material_id` INT NOT NULL ,
  PRIMARY KEY (`lecture_id`, `material_id`) ,
  INDEX `fk_lecture_has_material_material1_idx` (`material_id` ASC) ,
  INDEX `fk_lecture_has_material_lecture1_idx` (`lecture_id` ASC) ,
  CONSTRAINT `fk_lecture_has_material_lecture1`
    FOREIGN KEY (`lecture_id` )
    REFERENCES `phoenix`.`lecture` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lecture_has_material_material1`
    FOREIGN KEY (`material_id` )
    REFERENCES `phoenix`.`material` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`groupMaterial`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`groupMaterial` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`groupMaterial` (
  `group_id` INT NOT NULL ,
  `material_id` INT NOT NULL ,
  PRIMARY KEY (`group_id`, `material_id`) ,
  INDEX `fk_group_has_material_material1_idx` (`material_id` ASC) ,
  INDEX `fk_group_has_material_group1_idx` (`group_id` ASC) ,
  CONSTRAINT `fk_group_has_material_group1`
    FOREIGN KEY (`group_id` )
    REFERENCES `phoenix`.`group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_group_has_material_material1`
    FOREIGN KEY (`material_id` )
    REFERENCES `phoenix`.`material` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`submission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`submission` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`submission` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `submissionDate` DATETIME NOT NULL ,
  `status` INT NOT NULL ,
  `controllStatus` INT NOT NULL ,
  `controllMessage` TEXT NULL ,
  `task_exercise_sheet_pool_id` INT NOT NULL ,
  `task_task_id` INT UNSIGNED NOT NULL ,
  `exercise_sheet_exercise_sheet_id` INT NOT NULL ,
  `exercise_sheet_group_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_submission_task1_idx` (`task_exercise_sheet_pool_id` ASC, `task_task_id` ASC) ,
  INDEX `fk_submission_exercise_sheet1_idx` (`exercise_sheet_exercise_sheet_id` ASC, `exercise_sheet_group_id` ASC) ,
  CONSTRAINT `fk_submission_task1`
    FOREIGN KEY (`task_exercise_sheet_pool_id` , `task_task_id` )
    REFERENCES `phoenix`.`task` (`exercise_sheet_pool_id` , `task_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_submission_exercise_sheet1`
    FOREIGN KEY (`exercise_sheet_exercise_sheet_id` , `exercise_sheet_group_id` )
    REFERENCES `phoenix`.`exerciseSheet` (`exercise_sheet_id` , `group_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`submissionFiles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`submissionFiles` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`submissionFiles` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `content` TEXT NOT NULL ,
  `filename` VARCHAR(64) NOT NULL ,
  `submission_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_submissionFiles_submission1_idx` (`submission_id` ASC) ,
  CONSTRAINT `fk_submissionFiles_submission1`
    FOREIGN KEY (`submission_id` )
    REFERENCES `phoenix`.`submission` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`sampleSolution`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`sampleSolution` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`sampleSolution` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `author` VARCHAR(64) NOT NULL ,
  `task_exercise_sheet_pool_id` INT NOT NULL ,
  `task_task_id` INT UNSIGNED NOT NULL ,
  `lecture_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_sampleSolution_task1_idx` (`task_exercise_sheet_pool_id` ASC, `task_task_id` ASC) ,
  INDEX `fk_sampleSolution_lecture1_idx` (`lecture_id` ASC) ,
  CONSTRAINT `fk_sampleSolution_task1`
    FOREIGN KEY (`task_exercise_sheet_pool_id` , `task_task_id` )
    REFERENCES `phoenix`.`task` (`exercise_sheet_pool_id` , `task_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sampleSolution_lecture1`
    FOREIGN KEY (`lecture_id` )
    REFERENCES `phoenix`.`lecture` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`sampleSolutionFile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`sampleSolutionFile` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`sampleSolutionFile` (
  `id` INT NOT NULL ,
  `content` TEXT NOT NULL ,
  `fileName` VARCHAR(45) NOT NULL ,
  `sampleSolution_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_sampleSolutionFile_sampleSolution1_idx` (`sampleSolution_id` ASC) ,
  CONSTRAINT `fk_sampleSolutionFile_sampleSolution1`
    FOREIGN KEY (`sampleSolution_id` )
    REFERENCES `phoenix`.`sampleSolution` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`automaticTask`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`automaticTask` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`automaticTask` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `backend` VARCHAR(45) NOT NULL ,
  `taskPool_id` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_automaticTask_taskPool1_idx` (`taskPool_id` ASC) ,
  CONSTRAINT `fk_automaticTask_taskPool1`
    FOREIGN KEY (`taskPool_id` )
    REFERENCES `phoenix`.`taskPool` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `phoenix`.`automaticTaskFiles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `phoenix`.`automaticTaskFiles` ;

CREATE  TABLE IF NOT EXISTS `phoenix`.`automaticTaskFiles` (
  `id` INT NOT NULL ,
  `text` TEXT NOT NULL ,
  `unitTest` TEXT NULL ,
  `automaticTask_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_automaticTaskFiles_automaticTask1_idx` (`automaticTask_id` ASC) ,
  CONSTRAINT `fk_automaticTaskFiles_automaticTask1`
    FOREIGN KEY (`automaticTask_id` )
    REFERENCES `phoenix`.`automaticTask` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

USE `phoenix` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `phoenix`.`defaultRole`
-- -----------------------------------------------------
START TRANSACTION;
USE `phoenix`;
INSERT INTO `phoenix`.`defaultRole` (`id`, `name`, `inheritatedRole`) VALUES (1, 'guest', -1);
INSERT INTO `phoenix`.`defaultRole` (`id`, `name`, `inheritatedRole`) VALUES (2, 'student', 1);
INSERT INTO `phoenix`.`defaultRole` (`id`, `name`, `inheritatedRole`) VALUES (3, 'tutor', 2);
INSERT INTO `phoenix`.`defaultRole` (`id`, `name`, `inheritatedRole`) VALUES (4, 'exerciseLeader', 3);
INSERT INTO `phoenix`.`defaultRole` (`id`, `name`, `inheritatedRole`) VALUES (5, 'groupLeader', 4);
INSERT INTO `phoenix`.`defaultRole` (`id`, `name`, `inheritatedRole`) VALUES (6, 'lectureLeader', 5);
INSERT INTO `phoenix`.`defaultRole` (`id`, `name`, `inheritatedRole`) VALUES (7, 'instanceAdmin', 6);
INSERT INTO `phoenix`.`defaultRole` (`id`, `name`, `inheritatedRole`) VALUES (8, 'admin', 7);

COMMIT;
