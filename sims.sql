CREATE DATABASE  IF NOT EXISTS `sims` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sims`;
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sims
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `academy`
--

DROP TABLE IF EXISTS `academy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `academy` (
  `selectFlag` tinyint DEFAULT NULL COMMENT 'course selection flag',
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'academy id',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'academy name',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `academy`
--

LOCK TABLES `academy` WRITE;
/*!40000 ALTER TABLE `academy` DISABLE KEYS */;
INSERT INTO `academy` VALUES (0,1,'FBM'),(0,2,'DBM'),(0,3,'SCC'),(0,4,'FHS');
/*!40000 ALTER TABLE `academy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `academy_major`
--

DROP TABLE IF EXISTS `academy_major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `academy_major` (
  `academyId` int NOT NULL COMMENT 'academy id',
  `majorId` int NOT NULL COMMENT 'major id',
  UNIQUE KEY `academy_major_idx` (`academyId`,`majorId`) USING BTREE,
  KEY `academy_major_2` (`majorId`) USING BTREE,
  CONSTRAINT `academy_major_2` FOREIGN KEY (`majorId`) REFERENCES `major` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `academy_major_ibfk_1` FOREIGN KEY (`academyId`) REFERENCES `academy` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `academy_major`
--

LOCK TABLES `academy_major` WRITE;
/*!40000 ALTER TABLE `academy_major` DISABLE KEYS */;
INSERT INTO `academy_major` VALUES (1,1),(2,2),(3,3),(4,4);
/*!40000 ALTER TABLE `academy_major` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'admin username',
  `name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'name',
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT 'avatar url',
  `sex` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'gender',
  `phoneNum` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'phone number',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT 'description',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `updateTIme` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('2030024424','Alex','https://cloud-img-1301075855.cos.ap-chengdu.myqcloud.com/sims/5081590838134141.jpg','male','17676469050','I love FYP!','2023-09-19 15:38:24','2023-10-10 19:34:35');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class` (
  `id` varchar(4) NOT NULL COMMENT 'section id',
  `name` varchar(255) NOT NULL COMMENT 'name',
  `count` int DEFAULT '0' COMMENT 'count',
  `courseId` int NOT NULL,
  `key` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`key`) USING BTREE,
  KEY `courseId_idx` (`courseId`),
  CONSTRAINT `courseId` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class`
--

LOCK TABLES `class` WRITE;
/*!40000 ALTER TABLE `class` DISABLE KEYS */;
INSERT INTO `class` VALUES ('1001','Machine learning',1,1,16),('1003','Fundation of Python',4,2,18),('1002','Calculus',3,4,19),('1002','Deep learning',2,5,20);
/*!40000 ALTER TABLE `class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'course id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'course name',
  `status` tinyint DEFAULT NULL COMMENT 'course type 1 is MR，2 is ME, 3 is FE',
  `credit` double(3,2) DEFAULT NULL COMMENT 'unit',
  `period` int DEFAULT NULL COMMENT 'period',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  `academyId` int DEFAULT NULL COMMENT 'academy id',
  `teacherId` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'teacher id',
  `total` int DEFAULT NULL COMMENT 'count',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `academy_course` (`academyId`) USING BTREE,
  KEY `teacher_course` (`teacherId`) USING BTREE,
  CONSTRAINT `academy_course` FOREIGN KEY (`academyId`) REFERENCES `academy` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'Machine learning',1,3.00,42,'2023-10-10 11:24:23','2023-12-10 14:12:34',1,'',40),(2,'Fundation of Python',2,3.00,42,'2023-10-10 19:55:36','2023-12-10 14:12:34',1,'',40),(4,'Calculus',3,3.00,14,'2023-11-15 14:00:43','2023-12-10 09:42:47',1,'',40),(5,'Deep learning',1,3.00,1600,'2023-12-06 09:50:00','2023-12-10 09:42:47',1,'',40),(6,'Fundation of C',1,3.00,24,'2023-12-06 09:50:00','2023-12-10 09:42:47',1,NULL,40);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_exam`
--

DROP TABLE IF EXISTS `course_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_exam` (
  `classId` varchar(4) NOT NULL COMMENT 'room id',
  `examId` int NOT NULL COMMENT 'exam id',
  KEY `course_exam_ibfk_2` (`examId`) USING BTREE,
  CONSTRAINT `course_exam_ibfk_2` FOREIGN KEY (`examId`) REFERENCES `exam_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_exam`
--

LOCK TABLES `course_exam` WRITE;
/*!40000 ALTER TABLE `course_exam` DISABLE KEYS */;
INSERT INTO `course_exam` VALUES ('1001',1);
/*!40000 ALTER TABLE `course_exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_prerequest`
--

DROP TABLE IF EXISTS `course_prerequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_prerequest` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `course_id` int NOT NULL,
  `prerequest_id` int NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_prerequest`
--

LOCK TABLES `course_prerequest` WRITE;
/*!40000 ALTER TABLE `course_prerequest` DISABLE KEYS */;
INSERT INTO `course_prerequest` VALUES (1,5,1),(2,5,2);
/*!40000 ALTER TABLE `course_prerequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_time`
--

DROP TABLE IF EXISTS `course_time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_time` (
  `id` int NOT NULL COMMENT 'period id',
  `weekth` varchar(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'hour',
  `day` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'Intra-week time',
  `hour` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_time`
--

LOCK TABLES `course_time` WRITE;
/*!40000 ALTER TABLE `course_time` DISABLE KEYS */;
INSERT INTO `course_time` VALUES (1,'3','Monday','15:00');
/*!40000 ALTER TABLE `course_time` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_to_time`
--

DROP TABLE IF EXISTS `course_to_time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_to_time` (
  `courseId` int NOT NULL COMMENT 'course id',
  `courseTimeId` int NOT NULL COMMENT 'period id',
  UNIQUE KEY `course_to_time_idx` (`courseId`,`courseTimeId`) USING BTREE,
  KEY `course_to_time_ibfk_2` (`courseTimeId`) USING BTREE,
  CONSTRAINT `course_to_time_ibfk_1` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `course_to_time_ibfk_2` FOREIGN KEY (`courseTimeId`) REFERENCES `course_time` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_to_time`
--

LOCK TABLES `course_to_time` WRITE;
/*!40000 ALTER TABLE `course_to_time` DISABLE KEYS */;
INSERT INTO `course_to_time` VALUES (1,1);
/*!40000 ALTER TABLE `course_to_time` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_info`
--

DROP TABLE IF EXISTS `exam_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_info` (
  `id` int NOT NULL COMMENT 'exam id',
  `courseId` int NOT NULL COMMENT 'course id',
  `time` datetime DEFAULT NULL COMMENT 'exam time',
  `place` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'exam room',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `exam_info_ibfk_1` (`courseId`) USING BTREE,
  CONSTRAINT `exam_info_ibfk_1` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_info`
--

LOCK TABLES `exam_info` WRITE;
/*!40000 ALTER TABLE `exam_info` DISABLE KEYS */;
INSERT INTO `exam_info` VALUES (1,1,'2023-10-10 17:02:00','T5-101');
/*!40000 ALTER TABLE `exam_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `major`
--

DROP TABLE IF EXISTS `major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `major` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'major id',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'major name',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `major`
--

LOCK TABLES `major` WRITE;
/*!40000 ALTER TABLE `major` DISABLE KEYS */;
INSERT INTO `major` VALUES (1,'CST'),(2,'DS'),(3,'MAD'),(4,'EBIS'),(5,'AM'),(6,'ACCT');
/*!40000 ALTER TABLE `major` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `major_course`
--

DROP TABLE IF EXISTS `major_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `major_course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `majorId` int NOT NULL,
  `courseId` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `majorId_idx` (`majorId`),
  KEY `courseId_idx` (`courseId`),
  CONSTRAINT `majorId` FOREIGN KEY (`majorId`) REFERENCES `major` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `major_course`
--

LOCK TABLES `major_course` WRITE;
/*!40000 ALTER TABLE `major_course` DISABLE KEYS */;
/*!40000 ALTER TABLE `major_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'manu id',
  `path` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'corresponding front-end route',
  `component` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'component name',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'navigation name',
  `iconCls` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'navigation icon',
  `metaId` int DEFAULT NULL COMMENT 'meta id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `menu_ibfk_1` (`metaId`) USING BTREE,
  CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`metaId`) REFERENCES `meta` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'','Index','InFo Manage','el-icon-s-custom',1),(2,'/personalInfo','PersonalInfoManage','User Manage',NULL,1),(3,'/teacherInfoManager','TeacherInfoManage','Teacher Info Manage',NULL,1),(4,'','Index','Teaching Manage','el-icon-menu',1),(6,'/publish','PublishBoard','Publish Manage',NULL,1),(7,'/courseManager','CourseManage','Course Manage',NULL,1),(8,'','Index','Grade Manage','el-icon-menu',1),(9,'/scoreInfo','ScoreInfo','Grade InFo',NULL,1),(10,'/uploadScore','ScoreUpload','Grade Upload',NULL,1),(11,'','Index','More','el-icon-more',1),(12,'/center','Center','Portal',NULL,1),(13,'/scoreArchieve','ScoreArchive','Grade Statistics',NULL,1),(14,'/studentInfoManage','StudentInfoManage','Student Info Manage',NULL,1),(15,'','Index','Campus','el-icon-s-home',1),(16,'/home','Home','Home',NULL,1),(17,'/academyManage','AcademyManage','Academy List',NULL,1),(18,'','Index','Personal InFo','el-icon-s-custom',1),(19,'/studentInfo','StudentInfo','Info Search',NULL,1),(20,'','Index','Course Selection','el-icon-star-on\r\n',1),(21,'/selectCourse','SelectCourse','Student Course Selection',NULL,1);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meta`
--

DROP TABLE IF EXISTS `meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meta` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'meta id',
  `keepAlive` tinyint NOT NULL DEFAULT '0' COMMENT 'keep alive',
  `requireAuth` tinyint NOT NULL DEFAULT '1' COMMENT 'require to login',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta`
--

LOCK TABLES `meta` WRITE;
/*!40000 ALTER TABLE `meta` DISABLE KEYS */;
INSERT INTO `meta` VALUES (1,1,1),(2,0,1),(3,1,0),(4,0,0);
/*!40000 ALTER TABLE `meta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice_board`
--

DROP TABLE IF EXISTS `notice_board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice_board` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'title',
  `published` tinyint DEFAULT NULL COMMENT 'Published status',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT 'context',
  `typeName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'notice type',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'creat time',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice_board`
--

LOCK TABLES `notice_board` WRITE;
/*!40000 ALTER TABLE `notice_board` DISABLE KEYS */;
INSERT INTO `notice_board` VALUES (12,'Test AR Notification',1,'<p>This is a test ar notification.</p>','AR Notification','2023-10-11 07:35:38','2023-10-11 07:35:38');
/*!40000 ALTER TABLE `notice_board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parent_menus`
--

DROP TABLE IF EXISTS `parent_menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parent_menus` (
  `parentId` int NOT NULL COMMENT 'parent id',
  `childId` int NOT NULL COMMENT 'child id',
  UNIQUE KEY `parent_menu_idx` (`parentId`,`childId`) USING BTREE,
  KEY `parent_menu_parentFK` (`parentId`) USING BTREE,
  KEY `parent_menu_childFK` (`childId`) USING BTREE,
  CONSTRAINT `parent_menu_childFK` FOREIGN KEY (`childId`) REFERENCES `menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `parent_menu_parentFK` FOREIGN KEY (`parentId`) REFERENCES `menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parent_menus`
--

LOCK TABLES `parent_menus` WRITE;
/*!40000 ALTER TABLE `parent_menus` DISABLE KEYS */;
INSERT INTO `parent_menus` VALUES (1,2),(1,3),(1,14),(4,6),(4,7),(4,17),(8,9),(8,10),(8,13),(11,12),(15,16),(18,19),(20,21);
/*!40000 ALTER TABLE `parent_menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persistent_logins`
--

DROP TABLE IF EXISTS `persistent_logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `series` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persistent_logins`
--

LOCK TABLES `persistent_logins` WRITE;
/*!40000 ALTER TABLE `persistent_logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `persistent_logins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registry`
--

DROP TABLE IF EXISTS `registry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registry` (
  `uid` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'user id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `status` int NOT NULL COMMENT 'user status',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'user password',
  `locked` tinyint NOT NULL DEFAULT '0' COMMENT 'lock status',
  `enabled` tinyint DEFAULT NULL COMMENT 'whether used or not',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registry`
--

LOCK TABLES `registry` WRITE;
/*!40000 ALTER TABLE `registry` DISABLE KEYS */;
INSERT INTO `registry` VALUES ('2030024424','Alex',1,'$2a$10$wcb.jdsbu7vRm2n3eA43Bujsk0s7CEZOzJPw38.2On4y4fonOuKam',0,1),('2301001003','TestStudent',2,'$2a$10$l4k/kTJi/2eA19XPktC84uXs2LUbMGxFOmX6JM4uYg1n1sc.cvzpG',0,1),('23101','ATeacher',3,'$2a$10$o6Db46eP2us.TO/ao05p8uP7mHw9Pm4jw2B/.PBdvFYA.oq7JlEcW',0,1);
/*!40000 ALTER TABLE `registry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'role id',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'role name',
  `description` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'description',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_admin','admin'),(2,'ROLE_teacher','teacher'),(3,'ROLE_student','student'),(4,'ROLE_user','basic user');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
  `id` bigint NOT NULL COMMENT 'grade id',
  `studentId` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'user id',
  `courseId` int DEFAULT NULL COMMENT 'course id',
  `grade` int DEFAULT NULL COMMENT 'grade',
  `examTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `score_ibfk_2` (`courseId`) USING BTREE,
  KEY `socre_ibfk_1` (`studentId`) USING BTREE,
  CONSTRAINT `score_ibfk_2` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `socre_ibfk_1` FOREIGN KEY (`studentId`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
INSERT INTO `score` VALUES (1,'2301001003',6,89,'2023-10-10 19:09:00','2023-12-06 18:42:30'),(2,'2301001003',2,82,'2023-10-10 19:09:00','2023-12-06 18:50:47'),(3,'2301001003',4,76,'2023-10-10 19:09:00','2023-12-06 18:50:47');
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'student id',
  `name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'name',
  `nation` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'nation',
  `sex` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'gender\r\n',
  `age` int DEFAULT NULL COMMENT 'age',
  `politicsStatus` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'Student leader?',
  `idCard` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'id number',
  `phoneNum` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'phone number',
  `email` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'e-mail',
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT 'profile url',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT 'description',
  `academyId` int DEFAULT NULL COMMENT 'academy id',
  `majorId` int DEFAULT NULL COMMENT 'major id',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'enrollment time',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `student_ibfk_2` (`academyId`) USING BTREE,
  KEY `student_ibfk_3` (`majorId`) USING BTREE,
  CONSTRAINT `student_ibfk_2` FOREIGN KEY (`academyId`) REFERENCES `academy` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `student_ibfk_3` FOREIGN KEY (`majorId`) REFERENCES `major` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('2301001003','TestStudent',NULL,'male',21,NULL,'123456',NULL,'teststudent@testmail.com','','',1,1,'2023-11-15 00:00:00','2023-11-15 13:57:34');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_class`
--

DROP TABLE IF EXISTS `student_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_class` (
  `studentId` varchar(15) NOT NULL,
  `classId` varchar(4) DEFAULT NULL,
  `courseId` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `courseId` (`courseId`),
  CONSTRAINT `student_class_ibfk_1` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_class`
--

LOCK TABLES `student_class` WRITE;
/*!40000 ALTER TABLE `student_class` DISABLE KEYS */;
INSERT INTO `student_class` VALUES ('2301001003','1001',1,1),('2301001003','1003',2,9);
/*!40000 ALTER TABLE `student_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_course`
--

DROP TABLE IF EXISTS `student_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_course` (
  `studentId` varchar(15) DEFAULT NULL COMMENT 'student id',
  `courseId` int DEFAULT NULL COMMENT 'course id',
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `student_course_2` (`courseId`) USING BTREE,
  KEY `student_course_1` (`studentId`),
  CONSTRAINT `student_course_1` FOREIGN KEY (`studentId`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `student_course_2` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_course`
--

LOCK TABLES `student_course` WRITE;
/*!40000 ALTER TABLE `student_course` DISABLE KEYS */;
INSERT INTO `student_course` VALUES ('2301001003',1,1),('2301001003',2,14);
/*!40000 ALTER TABLE `student_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `id` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'teacher id',
  `name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'name',
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT 'profile url',
  `sex` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'gender',
  `idCard` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'id number',
  `politicsStatus` varchar(30) DEFAULT NULL COMMENT 'position',
  `phoneNum` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'phone number',
  `email` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'e-mail',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT 'description',
  `academyId` int DEFAULT NULL COMMENT 'academy id',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `teacher_ibfk_1` (`academyId`) USING BTREE,
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`academyId`) REFERENCES `academy` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES ('00000','No Teacher','','Male','000000',NULL,NULL,'NoMail@mail.com','',1,'2023-11-15 17:41:38','2023-11-15 17:41:38'),('1312','Susan','','fema','277128129362912873','Assistant professor','212837182738','21327256@qq,com','i am a good teacher',1,'2023-10-10 17:59:41','2023-10-10 17:59:41'),('23101','ATeacher','','Male','123456',NULL,NULL,'testteacher@testmail.com','',1,'2023-11-14 00:00:00','2023-11-14 13:03:32');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher_course`
--

DROP TABLE IF EXISTS `teacher_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher_course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `courseId` int NOT NULL,
  `classId` varchar(4) NOT NULL,
  `teacherId` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `classId_idx` (`classId`),
  KEY `courseId` (`courseId`),
  CONSTRAINT `teacher_course_ibfk_1` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher_course`
--

LOCK TABLES `teacher_course` WRITE;
/*!40000 ALTER TABLE `teacher_course` DISABLE KEYS */;
INSERT INTO `teacher_course` VALUES (1,1,'1001','1312'),(2,1,'1002','23101'),(3,2,'1003','1312');
/*!40000 ALTER TABLE `teacher_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_menu`
--

DROP TABLE IF EXISTS `user_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_menu` (
  `status` int NOT NULL COMMENT 'user status',
  `menuId` int NOT NULL COMMENT 'menu id',
  UNIQUE KEY `user_menu_idx` (`status`,`menuId`) USING BTREE,
  KEY `user_menu_ibfk_1` (`menuId`) USING BTREE,
  CONSTRAINT `user_menu_ibfk_1` FOREIGN KEY (`menuId`) REFERENCES `menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_menu`
--

LOCK TABLES `user_menu` WRITE;
/*!40000 ALTER TABLE `user_menu` DISABLE KEYS */;
INSERT INTO `user_menu` VALUES (1,1),(1,4),(1,8),(1,11),(2,11),(1,15),(2,15),(2,18),(2,20);
/*!40000 ALTER TABLE `user_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `userId` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'user id',
  `roleId` int NOT NULL COMMENT 'role id',
  UNIQUE KEY `user_role_idx` (`userId`,`roleId`) USING BTREE,
  KEY `user_role_ibfk_2` (`roleId`) USING BTREE,
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `registry` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES ('2030024424',1),('23101',2),('2301001003',3),('2030024424',4),('2301001003',4),('23101',4);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-12 19:09:06
