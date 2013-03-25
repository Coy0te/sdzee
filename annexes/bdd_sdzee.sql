-- MySQL dump 10.13  Distrib 5.5.29, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: bdd_sdzee
-- ------------------------------------------------------
-- Server version	5.5.29-0ubuntu0.12.04.2

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `titre` (`titre`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_categorie`
--

LOCK TABLES `forum_categorie` WRITE;
/*!40000 ALTER TABLE `forum_categorie` DISABLE KEYS */;
INSERT INTO `forum_categorie` VALUES (1,'Site Web','2013-03-21 16:13:08'),(2,'Programmation','2013-03-21 16:13:09'),(3,'Systèmes d\'exploitatiom','2013-03-21 16:13:09'),(4,'Infographie','2013-03-21 16:13:09'),(5,'Matériel & logiciels','2013-03-21 16:13:09'),(6,'Jeux vidéo','2013-03-21 16:13:09'),(7,'Communauté des Zéros','2013-03-21 16:13:09'),(8,'Sciences','2013-03-21 16:13:09');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_reponse`
--

LOCK TABLES `forum_reponse` WRITE;
/*!40000 ALTER TABLE `forum_reponse` DISABLE KEYS */;
INSERT INTO `forum_reponse` VALUES (1,1,1,'C\'est toi qui sent la moule !',0,0,'2013-03-22 10:47:15','192.168.12.34');
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
  `soustitre` varchar(60) NOT NULL,
  `texte` text NOT NULL,
  `auteur` int(11) NOT NULL,
  `forum` int(11) NOT NULL,
  `dateCreation` datetime NOT NULL,
  `ferme` tinyint(1) DEFAULT '0',
  `sticky` tinyint(1) DEFAULT '0',
  `vues` int(11) DEFAULT '0',
  `votesPositifs` int(11) DEFAULT '0',
  `votesNegatifs` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_auteur_sujet` (`auteur`),
  KEY `fk_forum_sujet` (`forum`),
  CONSTRAINT `fk_auteur_sujet` FOREIGN KEY (`auteur`) REFERENCES `membre` (`id`),
  CONSTRAINT `fk_forum_sujet` FOREIGN KEY (`forum`) REFERENCES `forum_forum` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_sujet`
--

LOCK TABLES `forum_sujet` WRITE;
/*!40000 ALTER TABLE `forum_sujet` DISABLE KEYS */;
INSERT INTO `forum_sujet` VALUES (1,'Un sujet au pif','','Alors que dans la moule d\'argentine la teneur en mouléïte n\'est que de 6.7mg/L, dans une mole de moules du Bigoudène nous pouvons retrouver 647mg/L, ce qui signifie indubitablement la supériorité des moules fraîches sur les moules latines.',1,2,'2013-03-21 16:13:44',0,0,0,0,0);
/*!40000 ALTER TABLE `forum_sujet` ENABLE KEYS */;
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `pseudo` (`pseudo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membre`
--

LOCK TABLES `membre` WRITE;
/*!40000 ALTER TABLE `membre` DISABLE KEYS */;
INSERT INTO `membre` VALUES (1,'coyote@test.com','baSDuV8d+My8W/M84MTBK42acjjKWeY9RrfboLDH/R7ot72cCi/KkA==','Med',NULL,NULL,'2013-03-21 16:12:48',NULL);
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

-- Dump completed on 2013-03-25 12:27:45
