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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
ALTER TABLE status AUTO_INCREMENT = 1;
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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
ALTER TABLE tasks AUTO_INCREMENT = 1;
--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO tasks(title,description,created_on,updated_on,id_status,assignees) VALUES ('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0','Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5','2024-04-22 09:00:00','2024-04-22 09:00:00',1,'Assignees1Assignees2Assignees3'),('Repository',null,'2024-04-22 09:05:00','2024-04-22 14:00:00',2,null),('ดาต้าเบส','ສ້າງຖານຂໍ້ມູນ','2024-04-22 09:10:00','2024-04-25 00:00:00',3,'あなた、彼、彼女 (私ではありません)'),('_Infrastructure_','_Setup containers_','2024-04-22 09:15:00','2024-04-22 10:00:00',4,'ไก่งวง กับ เพนกวิน');
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-27 16:45:55

CREATE TABLE IF NOT EXISTS `karban`.`tasksV2` (
                                                  `id_task` INT NOT NULL AUTO_INCREMENT,
                                                  `title` VARCHAR(100) NOT NULL,
    `description` VARCHAR(500) NULL DEFAULT NULL,
    `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `task_status` VARCHAR(50) NOT NULL DEFAULT 'No Status',
    `assignees` VARCHAR(30) NULL DEFAULT NULL,
    PRIMARY KEY (`id_task`),
    INDEX `fk_Tasks_status_idx` (`task_status` ASC) VISIBLE,
    CONSTRAINT `fk_Tasks_statusV2`
    FOREIGN KEY (`task_status`)
    REFERENCES `karban`.`statusV2` (`status_name`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;



LOCK TABLES `tasksV2` WRITE;
INSERT INTO tasksV2(title,description,created_on,updated_on,task_status,assignees) VALUES
                                                                                       ('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0',
                                                                                        'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5','2024-04-22 09:00:00','2024-04-22 09:00:00','No Status','Assignees1Assignees2Assignees3'),
                                                                                       ('Repository',null,'2024-04-22 09:05:00','2024-04-22 14:00:00','To Do',null),
                                                                                       ('ดาต้าเบส','ສ້າງຖານຂໍ້ມູນ','2024-04-22 09:10:00','2024-04-25 00:00:00','Doing','あなた、彼、彼女 (私ではありません)'),
                                                                                       ('_Infrastructure_','_Setup containers_','2024-04-22 09:15:00','2024-04-22 10:00:00','Done','ไก่งวง กับ เพนกวิน');
UNLOCK TABLES;