-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: oop_proj
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answers`
--

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `answers` (
  `answerID` int(11) NOT NULL AUTO_INCREMENT,
  `questionID` int(11) NOT NULL,
  `quizID` int(11) NOT NULL,
  `answer` varchar(8000) DEFAULT NULL,
  PRIMARY KEY (`answerID`),
  KEY `fk_quizAnswers_quizQuestions1_idx` (`questionID`),
  KEY `fk_quizAnswers_quizzes1_idx` (`quizID`),
  CONSTRAINT `fk_quizAnswers_quizQuestions1` FOREIGN KEY (`questionID`) REFERENCES `questions` (`questionID`) ON DELETE CASCADE,
  CONSTRAINT `fk_quizAnswers_quizzes1` FOREIGN KEY (`quizID`) REFERENCES `quizzes` (`quizID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `categories` (
  `categoryID` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`categoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `friends` (
  `friendshipID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL COMMENT 'When inserting into this table, both friends should be included in the userID column (i.e. one friendship between user 1 and user 5 will create two rows: "1, 5" and "5, 1"',
  `frienduserID` int(11) NOT NULL,
  PRIMARY KEY (`friendshipID`),
  KEY `fk_friendships_users1_idx` (`userID`),
  KEY `fk_friendships_users2_idx` (`frienduserID`),
  CONSTRAINT `fk_friendships_users1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE,
  CONSTRAINT `fk_friendships_users2` FOREIGN KEY (`frienduserID`) REFERENCES `users` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `history` (
  `quizHistoryID` int(11) NOT NULL AUTO_INCREMENT,
  `quizID` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `dateTaken` datetime DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `completed?` tinyint(1) DEFAULT NULL,
  `timeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`quizHistoryID`),
  KEY `fk_quizHistory_quizzes1_idx` (`quizID`),
  KEY `fk_quizHistory_users1_idx` (`userID`),
  CONSTRAINT `fk_quizHistory_quizzes1` FOREIGN KEY (`quizID`) REFERENCES `quizzes` (`quizID`) ON DELETE CASCADE,
  CONSTRAINT `fk_quizHistory_users1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `messages` (
  `messageID` int(11) NOT NULL AUTO_INCREMENT,
  `messageType` tinyint(4) DEFAULT NULL,
  `toUserID` int(11) NOT NULL,
  `fromUserID` int(11) NOT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `content` varchar(8000) DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `messageRead` tinyint(1) DEFAULT NULL,
  `toUserDeleted` tinyint(1) DEFAULT NULL,
  `fromUserDeleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`messageID`),
  KEY `fk_mailMessage_users_idx` (`toUserID`),
  KEY `fk_mailMessage_users1_idx` (`fromUserID`),
  CONSTRAINT `fk_mailMessage_users` FOREIGN KEY (`toUserID`) REFERENCES `users` (`userID`) ON DELETE CASCADE,
  CONSTRAINT `fk_mailMessage_users1` FOREIGN KEY (`fromUserID`) REFERENCES `users` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `questions` (
  `questionID` int(11) NOT NULL AUTO_INCREMENT,
  `quizID` int(11) NOT NULL,
  `questionTypeID` int(11) NOT NULL,
  `question` varchar(8000) DEFAULT NULL,
  `questionNumber` int(11) DEFAULT NULL,
  PRIMARY KEY (`questionID`),
  KEY `fk_quizQuestions_quizzes1_idx` (`quizID`),
  KEY `fk_quizQuestions_questionTypes1_idx` (`questionTypeID`),
  CONSTRAINT `fk_quizQuestions_questionTypes1` FOREIGN KEY (`questionTypeID`) REFERENCES `questiontypes` (`questionTypeID`) ON DELETE CASCADE,
  CONSTRAINT `fk_quizQuestions_quizzes1` FOREIGN KEY (`quizID`) REFERENCES `quizzes` (`quizID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quizcategories`
--

DROP TABLE IF EXISTS `quizcategories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `quizcategories` (
  `quizCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `quizID` int(11) NOT NULL,
  `categoryID` int(11) NOT NULL,
  PRIMARY KEY (`quizCategoryID`),
  KEY `fk_quizCategories_quizzes1_idx` (`quizID`),
  KEY `fk_quizCategories_categories1_idx` (`categoryID`),
  CONSTRAINT `fk_quizCategories_categories1` FOREIGN KEY (`categoryID`) REFERENCES `categories` (`categoryID`) ON DELETE CASCADE,
  CONSTRAINT `fk_quizCategories_quizzes1` FOREIGN KEY (`quizID`) REFERENCES `quizzes` (`quizID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quiztags`
--

DROP TABLE IF EXISTS `quiztags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `quiztags` (
  `quizTagID` int(11) NOT NULL AUTO_INCREMENT,
  `quizID` int(11) NOT NULL,
  `tagID` int(11) NOT NULL,
  PRIMARY KEY (`quizTagID`),
  KEY `fk_quizTags_tags1_idx` (`tagID`),
  KEY `fk_quizTags_quizzes1_idx` (`quizID`),
  CONSTRAINT `fk_quizTags_quizzes1` FOREIGN KEY (`quizID`) REFERENCES `quizzes` (`quizID`) ON DELETE CASCADE,
  CONSTRAINT `fk_quizTags_tags1` FOREIGN KEY (`tagID`) REFERENCES `tags` (`tagID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quizzes`
--

DROP TABLE IF EXISTS `quizzes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `quizzes` (
  `quizID` int(11) NOT NULL AUTO_INCREMENT,
  `quizName` varchar(255) DEFAULT NULL,
  `quizCreation` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `quizCreatorUserID` int(11) NOT NULL,
  `singlePage?` tinyint(1) DEFAULT NULL,
  `randomOrder?` tinyint(1) DEFAULT NULL,
  `immediateCorrection?` tinyint(1) DEFAULT NULL,
  `practiceMode?` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`quizID`),
  KEY `fk_quizzes_users1_idx` (`quizCreatorUserID`),
  CONSTRAINT `fk_quizzes_users1` FOREIGN KEY (`quizCreatorUserID`) REFERENCES `users` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ratings` (
  `ratingID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `quizID` int(11) NOT NULL,
  `ratingValue` tinyint(4) DEFAULT NULL,
  `ratingReview` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ratingID`),
  KEY `fk_userQuizRatings_users1_idx` (`userID`),
  KEY `fk_userQuizRatings_quizzes1_idx` (`quizID`),
  CONSTRAINT `fk_userQuizRatings_quizzes1` FOREIGN KEY (`quizID`) REFERENCES `quizzes` (`quizID`) ON DELETE CASCADE,
  CONSTRAINT `fk_userQuizRatings_users1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scores`
--

DROP TABLE IF EXISTS `scores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `scores` (
  `scoreID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `quizID` int(11) NOT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`scoreID`),
  KEY `fk_scores_users1_idx` (`userID`),
  KEY `fk_scores_quizzes1_idx` (`quizID`),
  CONSTRAINT `fk_scores_quizzes1` FOREIGN KEY (`quizID`) REFERENCES `quizzes` (`quizID`) ON DELETE CASCADE,
  CONSTRAINT `fk_scores_users1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tags` (
  `tagID` int(11) NOT NULL AUTO_INCREMENT,
  `tagName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tagID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `types`
--

DROP TABLE IF EXISTS `types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `types` (
  `typeID` int(11) NOT NULL AUTO_INCREMENT,
  `typeName` varchar(10) DEFAULT NULL,
  `typeDescription` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`typeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_achievements`
--

DROP TABLE IF EXISTS `user_achievements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_achievements` (
  `userAchievementID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `achievementID` int(11) NOT NULL,
  `dateAchieved` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userAchievementID`),
  KEY `fk_userAchievements_users1_idx` (`userID`),
  KEY `fk_userAchievements_achievements1_idx` (`achievementID`),
  CONSTRAINT `fk_userAchievements_achievements1` FOREIGN KEY (`achievementID`) REFERENCES `achievements` (`achievementID`) ON DELETE CASCADE,
  CONSTRAINT `fk_userAchievements_users1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `dateJoined` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `password` varchar(255) DEFAULT NULL,
  `cookie` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usertypes`
--

DROP TABLE IF EXISTS `usertypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usertypes` (
  `userTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `typeID` int(11) NOT NULL,
  PRIMARY KEY (`userTypeID`),
  KEY `fk_userTypes_users1_idx` (`userID`),
  KEY `fk_userTypes_types1_idx` (`typeID`),
  CONSTRAINT `fk_userTypes_types1` FOREIGN KEY (`typeID`) REFERENCES `types` (`typeID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_userTypes_users1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-22 23:53:00
