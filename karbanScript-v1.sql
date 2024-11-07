-- MySQL dump 10.13  Distrib 8.3.0, for macos14.2 (x86_64)
--
-- Host: localhost    Database: karban
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `status`
# create database karban;
use karban;
DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
                          `id_status` int NOT NULL AUTO_INCREMENT,
                          `status` enum('NO_STATUS','TO_DO','DOING','DONE') NOT NULL,
                          PRIMARY KEY (`id_status`),
                          UNIQUE KEY `id_status_UNIQUE` (`id_status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO status(status) VALUES ('NO_STATUS'),('TO_DO'),('DOING'),('DONE');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks` (
                         `id_task` int NOT NULL AUTO_INCREMENT,
                         `title` varchar(100) NOT NULL,
                         `description` varchar(500) DEFAULT NULL,
                         `created_on` datetime DEFAULT CURRENT_TIMESTAMP,
                         `updated_on` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         `id_status` int NOT NULL,
                         `assignees` varchar(30) DEFAULT NULL,
                         PRIMARY KEY (`id_task`),
                         KEY `fk_Tasks_status_idx` (`id_status`),
                         CONSTRAINT `fk_Tasks_status` FOREIGN KEY (`id_status`) REFERENCES `status` (`id_status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO tasks(title,description,created_on,updated_on,id_status,assignees) VALUES ('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0','Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5','2024-04-22 09:00:00','2024-04-22 09:00:00',1,'Assignees1Assignees2Assignees3'),('Repository',null,'2024-04-22 09:05:00','2024-04-22 14:00:00',2,null),('ดาต้าเบส','ສ້າງຖານຂໍ້ມູນ','2024-04-22 09:10:00','2024-04-25 00:00:00',3,'あなた、彼、彼女 (私ではありません)'),('_Infrastructure_','_Setup containers_','2024-04-22 09:15:00','2024-04-22 10:00:00',4,'ไก่งวง กับ เพนกวิน');
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;



DROP TABLE IF EXISTS `color_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `color_status` (
                                `id_color` int NOT NULL AUTO_INCREMENT,
                                `color_name` VARCHAR(20) NOT NULL unique,
                                `color_hex` VARCHAR(6) NOT NULL unique,
                                PRIMARY KEY (`id_color`),
                                UNIQUE INDEX `id_status_UNIQUE` (`id_color` ASC) VISIBLE)
    ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `color_status` WRITE;
/*!40000 ALTER TABLE `color_status` DISABLE KEYS */;
INSERT INTO color_status(color_name,color_hex) VALUES ('gray','aeaca7'),('red','FF0000'),('yellow','EAB308'),('blue','3B82F6'),('green','16a34a'),('purple','713799'),('brown','9F8170'),('orange','FF8000'),('dark blue','000066'),('blood','C62D42'),('pink','FF1493'),('black','1E1E1E');
/*!40000 ALTER TABLE `color_status` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `statusV2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statusV2` (
                            `id_status` int NOT NULL AUTO_INCREMENT,
                            `status_name` VARCHAR(50) NOT NULL unique,
                            `id_color` int DEFAULT 1,
                            `status_description` VARCHAR(200) NULL,
                            `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id_status`),
                            INDEX `fk_Statuses_color_idx` (`id_color` ASC) VISIBLE,
                            CONSTRAINT `fk_Statuses_color`
                                FOREIGN KEY (`id_color`)
                                    REFERENCES `karban`.`color_status` (`id_color`))
    ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


LOCK TABLES `statusV2` WRITE;
/*!40000 ALTER TABLE `statusV2` DISABLE KEYS */;
INSERT INTO statusV2(status_name,status_description,id_color) VALUES ('No Status','The default status',1),('To Do','The task is included in the project',3),('In Progress','The task is being worked on',7),('Reviewing','The task is being reviewed',9),('Testing','The task is being tested',8),('Waiting','The task is waiting for a resource',4),('Done','Finished',5);
/*!40000 ALTER TABLE `statusV2` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `tasksV2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasksV2` (
                           `id_task` INT NOT NULL AUTO_INCREMENT,
                           `title` VARCHAR(100) NOT NULL,
                           `description` VARCHAR(500) NULL DEFAULT NULL,
                           `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `id_status` int NOT NULL DEFAULT 1,
                           `assignees` VARCHAR(30) NULL DEFAULT NULL,
                           PRIMARY KEY (`id_task`),
                           INDEX `fk_Tasks_status_idx` (`id_status` ASC) VISIBLE,
                           CONSTRAINT `fk_Tasks_statusV2`
                               FOREIGN KEY (`id_status`)
                                   REFERENCES `karban`.`statusV2` (`id_status`))
    ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


LOCK TABLES `tasksV2` WRITE;
INSERT INTO tasksV2(title,description,created_on,updated_on,id_status,assignees) VALUES
                                                                                     ('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0',
                                                                                      'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5','2024-04-22 09:00:00','2024-04-22 09:00:00',1,'Assignees1Assignees2Assignees3'),
                                                                                     ('Repository',null,'2024-04-22 09:05:00','2024-04-22 14:00:00',2,null),
                                                                                     ('ดาต้าเบส','ສ້າງຖານຂໍ້ມູນ','2024-04-22 09:10:00','2024-04-25 00:00:00',3,'あなた、彼、彼女 (私ではありません)'),
                                                                                     ('_Infrastructure_','_Setup containers_','2024-04-22 09:15:00','2024-04-22 10:00:00',4,'ไก่งวง กับ เพนกวิน');
UNLOCK TABLES;

CREATE TABLE `settings` (
                            `name_of_configure` VARCHAR(50) NOT NULL,
                            `value` INT NULL,
                            `enable` TINYINT(1) NOT NULL DEFAULT 1,
                            PRIMARY KEY (`name_of_configure`),
                            UNIQUE INDEX `id_setting_UNIQUE` (`name_of_configure` ASC) VISIBLE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO settings  (name_of_configure,value) VALUES ('limit_of_tasks',10);



-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema karban
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema karban
-- -----------------------------------------------------
# test enable task
# UPDATE `karban`.`boards` SET `enable_status_center` = b'0101' WHERE (`nano_id_board` = 'DBDG1CWE8r');
-- -----------------------------------------------------
-- Table `karban`.`boards`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `karban`.`boards` (
    `nano_id_board` VARCHAR(10) NOT NULL,
    `name` VARCHAR(120) NOT NULL,
    `enable_limits_task` BIT(1) NOT NULL DEFAULT 1,
    `public` BIT(1) NOT NULL DEFAULT 0,
    `limits_task` INT NOT NULL DEFAULT 10,
    `enable_status_center` BIT(4) NOT NULL DEFAULT b'1111',
    PRIMARY KEY (`nano_id_board`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


LOCK TABLES `boards` WRITE;
/*!40000 ALTER TABLE `boards` DISABLE KEYS */;
INSERT INTO boards(nano_id_board,name,enable_status_center) VALUES ('0000000000','default broad', b'0000');
INSERT INTO boards(nano_id_board,name) VALUES ('1111111111','Olarn Board');
/*!40000 ALTER TABLE `boards` ENABLE KEYS */;
UNLOCK TABLES;


-- -----------------------------------------------------
-- Table `karban`.`statusV3`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `karban`.`statusV3` (
                                                   `id_status` INT NOT NULL AUTO_INCREMENT,
    `status_name` VARCHAR(50) NOT NULL,
    `id_color` INT NOT NULL DEFAULT '1',
    `status_description` VARCHAR(200) NULL DEFAULT NULL,
    `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `nano_id_board` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id_status`),
    INDEX `fk_Statuses_color_idx` (`id_color` ASC) VISIBLE,
    INDEX `fk_statusV2_boards1_idx` (`nano_id_board` ASC) VISIBLE,
    CONSTRAINT `fk_Statuses_color_v3`
    FOREIGN KEY (`id_color`)
    REFERENCES `karban`.`color_status` (`id_color`),
    CONSTRAINT `fk_statusV3_boards1`
    FOREIGN KEY (`nano_id_board`)
    REFERENCES `karban`.`boards` (`nano_id_board`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

LOCK TABLES `statusV3` WRITE;
/*!40000 ALTER TABLE `statusV3` DISABLE KEYS */;
INSERT INTO statusV3(nano_id_board,status_name,status_description,id_color) VALUES ('0000000000','No Status','The default status',1),('0000000000','To Do','The task is included in the project',3),('0000000000','Doing','The task is being worked on',7),('0000000000','Done','Finished',5);
/*!40000 ALTER TABLE `statusV3` ENABLE KEYS */;
UNLOCK TABLES;

-- -----------------------------------------------------
-- Table `karban`.`center_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `karban`.`center_status` (
                                                        `id_status` INT NOT NULL,
                                                        `enable_config` BIT(1) NOT NULL,
    INDEX `fk_center_status_statusV21_idx` (`id_status` ASC) VISIBLE,
    PRIMARY KEY (`id_status`),
    CONSTRAINT `fk_center_status_statusV21`
    FOREIGN KEY (`id_status`)
    REFERENCES `karban`.`statusV3` (`id_status`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

LOCK TABLES `center_status` WRITE;
/*!40000 ALTER TABLE `center_status` DISABLE KEYS */;
INSERT INTO center_status(id_status,enable_config) VALUES (1,0),(2,1),(3,1),(4,0);
/*!40000 ALTER TABLE `center_status` ENABLE KEYS */;
UNLOCK TABLES;

-- -----------------------------------------------------
-- Table `karban`.`tasksV3`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `karban`.`tasksV3` (
                                                  `id_task` INT NOT NULL AUTO_INCREMENT,
                                                  `title` VARCHAR(100) NOT NULL,
    `description` VARCHAR(500) NULL DEFAULT NULL,
    `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `id_status` INT NOT NULL DEFAULT '1',
    `assignees` VARCHAR(30) NULL DEFAULT NULL,
    `nano_id_board` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id_task`),
    INDEX `fk_Tasks_status_idx` (`id_status` ASC) VISIBLE,
    INDEX `fk_tasksV2_boards1_idx` (`nano_id_board` ASC) VISIBLE,
    CONSTRAINT `fk_Tasks_statusV3`
    FOREIGN KEY (`id_status`)
    REFERENCES `karban`.`statusV3` (`id_status`),
    CONSTRAINT `fk_tasksV3_boards1`
    FOREIGN KEY (`nano_id_board`)
    REFERENCES `karban`.`boards` (`nano_id_board`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

LOCK TABLES `tasksV3` WRITE;
INSERT INTO tasksV3(nano_id_board,title,description,created_on,updated_on,id_status,assignees) VALUES
                                                                                     ('1111111111','TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0',
                                                                                      'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5','2024-04-22 09:00:00','2024-04-22 09:00:00',1,'Assignees1Assignees2Assignees3'),
                                                                                     ('1111111111','Repository',null,'2024-04-22 09:05:00','2024-04-22 14:00:00',2,null),
                                                                                     ('1111111111','ดาต้าเบส','ສ້າງຖານຂໍ້ມູນ','2024-04-22 09:10:00','2024-04-25 00:00:00',3,'あなた、彼、彼女 (私ではありません)'),
                                                                                     ('1111111111','_Infrastructure_','_Setup containers_','2024-04-22 09:15:00','2024-04-22 10:00:00',4,'ไก่งวง กับ เพนกวิน');
UNLOCK TABLES;


-- -----------------------------------------------------
-- Table `karban`.`share_board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `karban`.`share_board` (
                                                      `oid_user_share` VARCHAR(36) NOT NULL,
    `nano_id_board` VARCHAR(10) NOT NULL,
    `role` ENUM("OWNER", "WRITER", "READER") NOT NULL,
    `added_on` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`oid_user_share`, `nano_id_board`),
    INDEX `fk_share_board_boards1_idx` (`nano_id_board` ASC) VISIBLE,
    CONSTRAINT `fk_share_board_boards1`
    FOREIGN KEY (`nano_id_board`)
    REFERENCES `karban`.`boards` (`nano_id_board`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

    LOCK TABLES `share_board` WRITE;
/*!40000 ALTER TABLE `share_board` DISABLE KEYS */;
INSERT INTO share_board(oid_user_share,nano_id_board,role) VALUES ("2b2f94fd-68be-4ff2-8c67-cb35e139f6fb","1111111111","OWNER");
/*!40000 ALTER TABLE `share_board` ENABLE KEYS */;
UNLOCK TABLES;


CREATE TABLE IF NOT EXISTS `karban`.`request_collab` (
                                                      `oid_user_share` VARCHAR(36) NOT NULL,
                                                      `nano_id_board` VARCHAR(10) NOT NULL,
                                                      `role` ENUM("OWNER", "WRITER", "READER") NOT NULL,
                                                      `added_on` datetime DEFAULT CURRENT_TIMESTAMP,
                                                      PRIMARY KEY (`oid_user_share`, `nano_id_board`),
                                                      INDEX `fk_request_collab_boards1_idx` (`nano_id_board` ASC) VISIBLE,
                                                      CONSTRAINT `fk_request_collab_boards1`
                                                          FOREIGN KEY (`nano_id_board`)
                                                              REFERENCES `karban`.`boards` (`nano_id_board`)
                                                              ON DELETE NO ACTION
                                                              ON UPDATE NO ACTION)
    ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `karban`.`task_attachment` (
                                                         `id` VARCHAR(16) NOT NULL,
                                                         `id_task` INT NOT NULL,
                                                         `name` VARCHAR(100),
                                                         `type` VARCHAR(10),
                                                         `added_on` datetime DEFAULT CURRENT_TIMESTAMP,
                                                         PRIMARY KEY (`id`),
                                                         INDEX `fk_task_attachment_task_idx` (`id_task` ASC) VISIBLE,
                                                         CONSTRAINT `fk_task_attachment_tasksV3`
                                                             FOREIGN KEY (`id_task`)
                                                                 REFERENCES `karban`.`tasksV3` (`id_task`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION)
    ENGINE = InnoDB;