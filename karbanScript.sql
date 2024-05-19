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
--
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

# `color` enum('C_F8719D','C_EAB308','C_3B82F6','C_16a34a','c5','c6','c7','c8','c9','c10','c11','c12','c13','c14','c15','c16') NOT NULL,

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
INSERT INTO color_status(color_name,color_hex) VALUES ('gray','aeaca7'),('red','FF0000'),('yellow','EAB308'),('blue','3B82F6'),('green','16a34a'),('purple','713799'),('brown','9F8170'),('orange','FF8000'),('height blue','000066'),('blood','C62D42'),('pink','FF1493'),('black','1E1E1E');
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
INSERT INTO statusV2(status_name,status_description,id_color) VALUES ('No Status','The default status',1),('To Do',null,3),('Doing','Being worked on',4),('Done','Finished',5);
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
                            `value` INT NULL CHECK (`value` >= 10 AND `value` <= 30),
                            `enable` TINYINT(1) NOT NULL DEFAULT 1,
                            PRIMARY KEY (`name_of_configure`),
                            UNIQUE INDEX `id_setting_UNIQUE` (`name_of_configure` ASC) VISIBLE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO settings  (name_of_configure,value) VALUES ('limit_of_tasks',10)

