-- MySQL dump 10.13  Distrib 5.5.28, for osx10.6 (i386)
--
-- Host: localhost    Database: bdd_sdzee
-- ------------------------------------------------------
-- Server version	5.5.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `forum_categorie`
--

DROP TABLE IF EXISTS `forum_categorie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forum_categorie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titre` varchar(60) NOT NULL,
  `dateCreation` datetime NOT NULL,
  `position` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `titre` (`titre`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_categorie`
--

LOCK TABLES `forum_categorie` WRITE;
/*!40000 ALTER TABLE `forum_categorie` DISABLE KEYS */;
INSERT INTO `forum_categorie` VALUES (1,'Site Web','2013-03-21 16:13:08',1),(2,'Programmation','2013-03-21 16:13:09',2),(3,'Systèmes d\'exploitatiom','2013-03-21 16:13:09',3),(4,'Infographie','2013-03-21 16:13:09',4),(5,'Matériel & logiciels','2013-03-21 16:13:09',5),(6,'Jeux vidéo','2013-03-21 16:13:09',6),(7,'Communauté des Zéros','2013-03-21 16:13:09',7),(8,'Sciences','2013-03-21 16:13:09',8);
/*!40000 ALTER TABLE `forum_categorie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum_forum`
--

DROP TABLE IF EXISTS `forum_forum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forum_forum` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titre` varchar(60) NOT NULL,
  `description` varchar(255) NOT NULL,
  `categorie` int(11) NOT NULL,
  `dateCreation` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `titre` (`titre`),
  KEY `fk_categorie_forum` (`categorie`),
  CONSTRAINT `fk_categorie_forum` FOREIGN KEY (`categorie`) REFERENCES `forum_categorie` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_forum`
--

LOCK TABLES `forum_forum` WRITE;
/*!40000 ALTER TABLE `forum_forum` DISABLE KEYS */;
INSERT INTO `forum_forum` VALUES (1,'HTML / CSS','Vos questions sur la réalisation de sites web en HTML et CSS',1,'2013-03-21 16:13:09'),(2,'Javascript','Vos questions à propos de Javascript et AJAX',1,'2013-03-21 16:13:09'),(3,'PHP','Un souci avec le PHP ? Venez demander de l\'aide !',1,'2013-03-21 16:13:09'),(4,'Langage C','Vos questions sur le langage C',2,'2013-03-21 16:13:09'),(5,'Langage C++','Vos questions sur le langage C++',2,'2013-03-21 16:13:09'),(6,'C# .NET & VB .NET','Si vous programmez en C# ou Visual Basic, postez ici !',2,'2013-03-21 16:13:09'),(7,'Langage Java','...',2,'2013-03-21 16:13:09'),(8,'Langage Python','...',2,'2013-03-21 16:13:10'),(9,'Bases de données','...',2,'2013-03-21 16:13:10'),(10,'Mobile','...',2,'2013-03-21 16:13:10'),(11,'Autres langages','',2,'2013-03-21 16:13:10'),(12,'Windows','Un souci avec Windows ? Il y aura quelqu\'un pour vous répondre.',3,'2013-03-21 16:13:10'),(13,'Linux & FreeBSD','Vous avez un problème avec Linux ou FreeBSD ?',3,'2013-03-21 16:13:10'),(14,'Mac OS X','Une question à propos de Mac OS ? Vous êtes au bon endroit !',3,'2013-03-21 16:13:10'),(15,'Graphisme 3D','',4,'2013-03-21 16:13:10'),(16,'Graphisme 2D','',4,'2013-03-21 16:13:10'),(17,'Discussions informatiques','Un problème en informatique ? C\'est par ici que ça se passe !',5,'2013-03-21 16:13:10'),(18,'Choix du matériel & configuration','Vous voulez personnaliser votre configuration ? Par ici !',5,'2013-03-21 16:13:10'),(19,'Problèmes techniques','Un problème matériel ? Posez votre question !',5,'2013-03-21 16:13:10'),(20,'Discussions jeux vidéo','Toutes les discussions autour des jeux vidéo.',6,'2013-03-21 16:13:10'),(21,'Mapping & modding','Des questions sur la création de maps et de mods  ?',6,'2013-03-21 16:13:10');
/*!40000 ALTER TABLE `forum_forum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum_reponse`
--

DROP TABLE IF EXISTS `forum_reponse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forum_reponse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auteur` int(11) NOT NULL,
  `sujet` int(11) NOT NULL,
  `texte` text NOT NULL,
  `votesPositifs` int(11) DEFAULT '0',
  `votesNegatifs` int(11) DEFAULT '0',
  `dateCreation` datetime NOT NULL,
  `adresseIP` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_auteur_reponse` (`auteur`),
  KEY `fk_sujet_reponse` (`sujet`),
  CONSTRAINT `fk_auteur_reponse` FOREIGN KEY (`auteur`) REFERENCES `membre` (`id`),
  CONSTRAINT `fk_sujet_reponse` FOREIGN KEY (`sujet`) REFERENCES `forum_sujet` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_reponse`
--

LOCK TABLES `forum_reponse` WRITE;
/*!40000 ALTER TABLE `forum_reponse` DISABLE KEYS */;
INSERT INTO `forum_reponse` VALUES (1,1,1,'C\'est toi qui sent la moule !',0,0,'2013-03-22 10:47:15','192.168.12.34'),(2,2,1,'Essai de rÃ©ponse depuis le forum.',0,0,'2013-04-14 18:06:52','0:0:0:0:0:0:0:1%0'),(3,1,1,'Qu\'est-ce que c\'est que ce bordel...',0,0,'2013-04-15 18:06:52','0:0:0:0:0:0:0:1%0'),(4,2,1,'Bon, a priori les accents c\'est encore pas trop Ã§a... faudrait vÃ©rifier que l\'encodage par dÃ©faut est bien UTF-8, et si c\'est pas le cas, alors il faut utiliser un filtre de derriÃ¨re les fagots de chez OmniFaces :)',0,0,'2013-04-15 19:06:52','0:0:0:0:0:0:0:1%0'),(5,2,1,'C\'est encore tout niqué ou pas ? Ça va bien 5 minutes ces élugubrations...',0,0,'2013-04-15 22:30:01','127.0.0.1'),(6,1,1,'áàö Ó ç í crubluZor.',0,0,'2013-04-15 22:34:05','0:0:0:0:0:0:0:1%0'),(7,1,1,'k',0,0,'2013-04-15 22:47:28','0:0:0:0:0:0:0:1%0'),(8,2,1,'test 435',0,0,'2013-04-16 13:20:19','0:0:0:0:0:0:0:1%0'),(9,2,1,'On tente de répondre au sujet, avec des accents et on vérifie que la date s\'affiche correctement.',0,0,'2013-04-16 18:33:04','0:0:0:0:0:0:0:1%0'),(10,2,2,'Qu\'est-ce qu\'y dit?',0,0,'2013-04-16 21:20:00','0:0:0:0:0:0:0:1%0'),(11,2,3,'Les \r\nsauts\r\nde\r\nligne\r\n\r\na priori\r\nc\'est\r\n\r\nencore\r\n\r\n\r\n\r\npas\r\nça.',0,0,'2013-04-16 21:37:09','0:0:0:0:0:0:0:1%0'),(12,1,2,'Osef dude, osef.',0,0,'2013-04-16 21:42:46','0:0:0:0:0:0:0:1%0');
/*!40000 ALTER TABLE `forum_reponse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum_sujet`
--

DROP TABLE IF EXISTS `forum_sujet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forum_sujet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titre` varchar(60) NOT NULL,
  `sousTitre` varchar(80) DEFAULT NULL,
  `texte` text NOT NULL,
  `auteur` int(11) NOT NULL,
  `forum` int(11) NOT NULL,
  `dateCreation` datetime NOT NULL,
  `ferme` tinyint(1) DEFAULT '0',
  `sticky` tinyint(1) DEFAULT '0',
  `vues` int(11) DEFAULT '0',
  `votesPositifs` int(11) DEFAULT '0',
  `votesNegatifs` int(11) DEFAULT '0',
  `adresseIP` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_auteur_sujet` (`auteur`),
  KEY `fk_forum_sujet` (`forum`),
  CONSTRAINT `fk_auteur_sujet` FOREIGN KEY (`auteur`) REFERENCES `membre` (`id`),
  CONSTRAINT `fk_forum_sujet` FOREIGN KEY (`forum`) REFERENCES `forum_forum` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_sujet`
--

LOCK TABLES `forum_sujet` WRITE;
/*!40000 ALTER TABLE `forum_sujet` DISABLE KEYS */;
INSERT INTO `forum_sujet` VALUES (1,'Un sujet au pif','','Alors que dans la moule d\'argentine la teneur en mouléïte n\'est que de 6.7mg/L, dans une mole de moules du Bigoudène nous pouvons retrouver 647mg/L, ce qui signifie indubitablement la supériorité des moules fraîches sur les moules latines.',1,2,'2013-03-21 16:13:44',0,0,0,0,0,'192.168.1.2'),(2,'Essai de nouveau sujet','Pour la première fois...','OH my god they killed kenny!',1,2,'2013-04-16 21:19:15',NULL,NULL,NULL,NULL,NULL,'0:0:0:0:0:0:0:1%0'),(3,'Premier test en categorie HTML','ça pique!','Blabla bli lala.\r\n\r\nCoyote.',2,1,'2013-04-16 21:21:03',NULL,NULL,NULL,NULL,NULL,'0:0:0:0:0:0:0:1%0');
/*!40000 ALTER TABLE `forum_sujet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum_vote`
--

DROP TABLE IF EXISTS `forum_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forum_vote` (
  `id_membre` int(11) NOT NULL,
  `id_message` int(11) NOT NULL,
  `valeur` tinyint(1) NOT NULL DEFAULT '0',
  UNIQUE KEY `id_membre` (`id_membre`,`id_message`),
  KEY `fk_membre_vote` (`id_membre`),
  KEY `fk_message_vote` (`id_message`),
  CONSTRAINT `fk_membre_vote` FOREIGN KEY (`id_membre`) REFERENCES `membre` (`id`),
  CONSTRAINT `fk_message_vote` FOREIGN KEY (`id_message`) REFERENCES `forum_reponse` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_vote`
--

LOCK TABLES `forum_vote` WRITE;
/*!40000 ALTER TABLE `forum_vote` DISABLE KEYS */;
/*!40000 ALTER TABLE `forum_vote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membre`
--

DROP TABLE IF EXISTS `membre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `membre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(60) NOT NULL,
  `motDePasse` varchar(64) NOT NULL,
  `pseudo` varchar(20) NOT NULL,
  `prenom` varchar(20) DEFAULT NULL,
  `nom` varchar(20) DEFAULT NULL,
  `dateInscription` datetime NOT NULL,
  `dateDerniereConnexion` datetime DEFAULT NULL,
  `droits` int(2) DEFAULT '1',
  `avatar` varchar(50) DEFAULT NULL,
  `signature` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `pseudo` (`pseudo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membre`
--

LOCK TABLES `membre` WRITE;
/*!40000 ALTER TABLE `membre` DISABLE KEYS */;
INSERT INTO `membre` VALUES (1,'coyote@test.com','baSDuV8d+My8W/M84MTBK42acjjKWeY9RrfboLDH/R7ot72cCi/KkA==','Med',NULL,NULL,'2013-03-21 16:12:48',NULL,1,NULL,'Oh my god, they killed Kenny!'),(2,'coy@ote.fr','baSDuV8d+My8W/M84MTBK42acjjKWeY9RrfboLDH/R7ot72cCi/KkA==','Coyote',NULL,NULL,'2013-03-21 16:12:48',NULL,4,NULL,'Test de signature');
/*!40000 ALTER TABLE `membre` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-04-18 22:12:56
