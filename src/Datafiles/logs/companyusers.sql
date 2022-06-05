-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: companyusers
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
  `username` varchar(16) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `message` varchar(32) NOT NULL,
  `create_time` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientchatlog`
--

LOCK TABLES `clientchatlog` WRITE;
/*!40000 ALTER TABLE `clientchatlog` DISABLE KEYS */;
INSERT INTO `clientchatlog` VALUES ('George Doe','client@ABM.org','This is a test message','01:34');
/*!40000 ALTER TABLE `clientchatlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requestlogs`
--

DROP TABLE IF EXISTS `requestlogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requestlogs` (
  `username` varchar(16) NOT NULL,
  `email` varchar(255) NOT NULL,
  `tasktype` varchar(32) NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `taskname` varchar(225) NOT NULL,
  `taskdesc` varchar(225) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requestlogs`
--

LOCK TABLES `requestlogs` WRITE;
/*!40000 ALTER TABLE `requestlogs` DISABLE KEYS */;
/*!40000 ALTER TABLE `requestlogs` ENABLE KEYS */;
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
  `taskname` varchar(255) NOT NULL,
  `taskdesc` varchar(270) DEFAULT NULL,
  `taskhex` varchar(255) NOT NULL,
  `taskprogress` int NOT NULL,
  `section` int DEFAULT NULL,
  `tasksubject` varchar(255) DEFAULT NULL,
  UNIQUE KEY `taskid_UNIQUE` (`taskid`),
  UNIQUE KEY `idx_tasks_taskid` (`taskid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (1000,'First Row Test ','Example 1','Example Description ','#123d82',0,1,NULL),(1001,'Second Row Test','Example Name 2','This is an example task with more text','#9a0000',0,5,NULL),(1002,'Example ','Example 3: More Text','This is an example task with more text and more words','#00b050',100,4,NULL),(1003,'Example Near Limit Test','Example 4','This is an example task with more text and more words and even more text','#333f50',10,2,NULL),(6507,'Test','Created Task','This is created with the app.','#2d79ff',25,3,NULL),(9498,'Created test with long type','Created test with long name','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer volutpat lobortis velit, nec mollis sapien ornare nec. \nMorbi congue quam eget molestie ultrices. Fusce pharetra convallis aliquam. Nulla sagittis auctor euismod. Aliquam \neu volutpat mauris ye\n','#00b050',100,4,NULL);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teamchatlog`
--

DROP TABLE IF EXISTS `teamchatlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teamchatlog` (
  `username` varchar(16) NOT NULL,
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
INSERT INTO `teamchatlog` VALUES ('Alec Goodsir','admin@ABM.org','This is a test message.','03:06'),('Admin User','admin','1','03:11'),('Admin User','admin','t','03:47'),('Admin User','admin','Test','04:02'),('Admin User','admin','This is a test message','04:02'),('Admin User','admin','Test','05:04'),('Admin User','admin','Test','05:06'),('Scott Davidson','user@ABM.org','This is a test message','02:47'),('Admin User','admin','This is a test message','01:18'),('Admin User','admin','This is a test message to see how fast the messaging is.','01:20');
/*!40000 ALTER TABLE `teamchatlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userdata`
--

DROP TABLE IF EXISTS `userdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userdata` (
  `email` varchar(16) NOT NULL,
  `password` varchar(32) NOT NULL,
  `usertype` varchar(255) NOT NULL,
  `firstname` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `role` varchar(45) DEFAULT NULL,
  `department` varchar(45) DEFAULT NULL,
  `userID` int NOT NULL AUTO_INCREMENT,
  UNIQUE KEY `userID_UNIQUE` (`userID`),
  UNIQUE KEY `idx_userdata_email` (`email`),
  UNIQUE KEY `idx_userdata_userID` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userdata`
--

LOCK TABLES `userdata` WRITE;
/*!40000 ALTER TABLE `userdata` DISABLE KEYS */;
INSERT INTO `userdata` VALUES ('admin@ABM.org','password','ADMIN','Alec','Goodsir','Project Manager','Management',1),('user@ABM.org','password2','USER','Scott','Davidson','Programmer','Software',2),('client@ABM.org','password3','CLIENT','George','Doe','Client','N/A',3),('admin','test','ADMIN','Admin','User','Admin','N/A',4);
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

-- Dump completed on 2022-06-05 11:27:05
