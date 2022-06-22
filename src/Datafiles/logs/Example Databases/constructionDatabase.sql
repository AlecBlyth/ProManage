-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: companydatabase
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clientchatlog`
--

DROP TABLE IF EXISTS `clientchatlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientchatlog` (
  `username` varchar(50) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `message` text NOT NULL,
  `time` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientchatlog`
--

LOCK TABLES `clientchatlog` WRITE;
/*!40000 ALTER TABLE `clientchatlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `clientchatlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requests`
--

DROP TABLE IF EXISTS `requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requests` (
  `taskid` int NOT NULL,
  `tasktype` varchar(255) NOT NULL,
  `taskname` varchar(255) DEFAULT NULL,
  `taskdesc` varchar(270) DEFAULT NULL,
  `taskhex` varchar(255) NOT NULL,
  `taskprogress` int NOT NULL,
  `section` int NOT NULL,
  `tasksubject` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`taskid`),
  UNIQUE KEY `taskid_UNIQUE` (`taskid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests`
--

LOCK TABLES `requests` WRITE;
/*!40000 ALTER TABLE `requests` DISABLE KEYS */;
/*!40000 ALTER TABLE `requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks` (
  `taskid` int NOT NULL,
  `tasktype` varchar(255) NOT NULL,
  `taskname` varchar(255) DEFAULT NULL,
  `taskdesc` varchar(270) DEFAULT NULL,
  `taskhex` varchar(255) NOT NULL,
  `taskprogress` int NOT NULL,
  `section` int NOT NULL,
  `tasksubject` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`taskid`),
  UNIQUE KEY `taskid_UNIQUE` (`taskid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teamchatlog`
--

DROP TABLE IF EXISTS `teamchatlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teamchatlog` (
  `username` varchar(50) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `message` text NOT NULL,
  `time` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teamchatlog`
--

LOCK TABLES `teamchatlog` WRITE;
/*!40000 ALTER TABLE `teamchatlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `teamchatlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userdata`
--

DROP TABLE IF EXISTS `userdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userdata` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `email` varchar(320) DEFAULT NULL,
  `password` varchar(500) NOT NULL,
  `usertype` varchar(10) DEFAULT NULL,
  `firstname` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `role` varchar(45) DEFAULT NULL,
  `department` varchar(45) DEFAULT NULL,
  UNIQUE KEY `userID_UNIQUE` (`userID`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1007 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userdata`
--

LOCK TABLES `userdata` WRITE;
/*!40000 ALTER TABLE `userdata` DISABLE KEYS */;
INSERT INTO `userdata` VALUES (1000,'Benz@ConstructCorp.net','engine','ADMIN','Karl','Benz','Admin','IT'),(1001,'LeoVinci@ConstructCorp.net','monalisa','ADMIN','Leonardo','DaVinci','Manager','Management'),(1002,'HAyrton@ConstructCorp.net','ripple','ADMIN','Hertha','Ayrton','Safety Officer','Human Resources'),(1003,'Siemens@ConstructCorp.net','siemens','USER','Carl Wilhelm','Siemens','Electrician','Electrical'),(1004,'EMendelsohn@ConstructCorp.net','artdeco','USER','Erich','Mendelsohn','Plumber','Plumbing'),(1005,'JNash@ConstructCorp.net','neoclassic','USER','John','Nash','Foreman','Construction'),(1006,'RobStephenson@ConstructCorp.net','railway','USER','Robert','Stephenson','Bricklayer','Construction');
/*!40000 ALTER TABLE `userdata` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-22 14:13:06
