-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: bdd_sdzee
-- ------------------------------------------------------
-- Server version	5.5.31-0ubuntu0.12.04.1

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
  `titre` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `dateCreation` datetime NOT NULL,
  `position` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `titre` (`titre`),
  KEY `position_index` (`position`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
  `titre` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `categorie` int(11) NOT NULL,
  `dateCreation` datetime NOT NULL,
  `auth_view` tinyint(1) NOT NULL DEFAULT '0',
  `auth_post` tinyint(1) NOT NULL DEFAULT '0',
  `auth_topic` tinyint(1) NOT NULL DEFAULT '0',
  `auth_modo` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `titre` (`titre`),
  KEY `fk_categorie_forum` (`categorie`),
  CONSTRAINT `fk_categorie_forum` FOREIGN KEY (`categorie`) REFERENCES `forum_categorie` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_forum`
--

LOCK TABLES `forum_forum` WRITE;
/*!40000 ALTER TABLE `forum_forum` DISABLE KEYS */;
INSERT INTO `forum_forum` VALUES (1,'HTML / CSS','Vos questions sur la réalisation de sites web en HTML et CSS',1,'2013-03-21 16:13:09',0,0,0,0),(2,'Javascript','Vos questions à propos de Javascript et AJAX',1,'2013-03-21 16:13:09',0,0,0,0),(3,'PHP','Un souci avec le PHP ? Venez demander de l\'aide !',1,'2013-03-21 16:13:09',0,0,0,0),(4,'Langage C','Vos questions sur le langage C',2,'2013-03-21 16:13:09',0,0,0,0),(5,'Langage C++','Vos questions sur le langage C++',2,'2013-03-21 16:13:09',0,0,0,0),(6,'C# .NET & VB .NET','Si vous programmez en C# ou Visual Basic, postez ici !',2,'2013-03-21 16:13:09',0,0,0,0),(7,'Langage Java','...',2,'2013-03-21 16:13:09',0,0,0,0),(8,'Langage Python','...',2,'2013-03-21 16:13:10',0,0,0,0),(9,'Bases de données','...',2,'2013-03-21 16:13:10',0,0,0,0),(10,'Mobile','...',2,'2013-03-21 16:13:10',0,0,0,0),(11,'Autres langages','',2,'2013-03-21 16:13:10',0,0,0,0),(12,'Windows','Un souci avec Windows ? Il y aura quelqu\'un pour vous répondre.',3,'2013-03-21 16:13:10',0,0,0,0),(13,'Linux & FreeBSD','Vous avez un problème avec Linux ou FreeBSD ?',3,'2013-03-21 16:13:10',0,0,0,0),(14,'Mac OS X','Une question à propos de Mac OS ? Vous êtes au bon endroit !',3,'2013-03-21 16:13:10',0,0,0,0),(15,'Graphisme 3D','',4,'2013-03-21 16:13:10',0,0,0,0),(16,'Graphisme 2D','',4,'2013-03-21 16:13:10',0,0,0,0),(17,'Discussions informatiques','Un problème en informatique ? C\'est par ici que ça se passe !',5,'2013-03-21 16:13:10',0,0,0,0),(18,'Choix du matériel & configuration','Vous voulez personnaliser votre configuration ? Par ici !',5,'2013-03-21 16:13:10',0,0,0,0),(19,'Problèmes techniques','Un problème matériel ? Posez votre question !',5,'2013-03-21 16:13:10',0,0,0,0),(20,'Discussions jeux vidéo','Toutes les discussions autour des jeux vidéo.',6,'2013-03-21 16:13:10',0,0,0,0),(21,'Mapping & modding','Des questions sur la création de maps et de mods  ?',6,'2013-03-21 16:13:10',0,0,0,0);
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
  `texte` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `votesPositifs` int(11) NOT NULL DEFAULT '0',
  `votesNegatifs` int(11) NOT NULL DEFAULT '0',
  `dateCreation` datetime NOT NULL,
  `adresseIP` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_auteur_reponse` (`auteur`),
  KEY `fk_sujet_reponse` (`sujet`),
  CONSTRAINT `fk_auteur_reponse` FOREIGN KEY (`auteur`) REFERENCES `membre` (`id`),
  CONSTRAINT `fk_sujet_reponse` FOREIGN KEY (`sujet`) REFERENCES `forum_sujet` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_reponse`
--

LOCK TABLES `forum_reponse` WRITE;
/*!40000 ALTER TABLE `forum_reponse` DISABLE KEYS */;
INSERT INTO `forum_reponse` VALUES (1,1,1,'C\'est toi qui sent la moule !',1,1,'2013-03-22 10:47:15','192.168.12.34'),(2,2,1,'Test d\'un message content dés accents èn pagaïlle.',9,16,'2013-04-15 09:43:01','127.0.0.1'),(3,2,1,'caca',0,6,'2013-04-16 10:53:10','127.0.0.1'),(4,1,1,'test de la date du message.',0,2,'2013-04-16 16:41:54','127.0.0.1'),(5,2,11,'PErsonne ?. Damnéd.',0,0,'2013-04-18 15:53:54','127.0.0.1'),(6,2,1,'?????????',0,1,'2013-04-19 16:02:49','127.0.0.1'),(7,2,19,'Ajout d\'une réponse destinée à être modifiée à foison ensuite, pour tester l\'édition et la modération d\'une réponse.',0,0,'2013-05-06 11:38:35','127.0.0.1'),(8,2,1,'Le chinois ça a pas l\'air d\'avoir super bien marché...\r\n=> ???????????????? ???????????',0,0,'2013-05-06 11:50:12','127.0.0.1');
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
  `titre` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `sousTitre` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `texte` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `auteur` int(11) NOT NULL,
  `forum` int(11) NOT NULL,
  `dateCreation` datetime NOT NULL,
  `ferme` tinyint(1) NOT NULL DEFAULT '0',
  `sticky` tinyint(1) NOT NULL DEFAULT '0',
  `resolu` tinyint(1) DEFAULT '0',
  `vues` int(11) NOT NULL DEFAULT '0',
  `votesPositifs` int(11) NOT NULL DEFAULT '0',
  `votesNegatifs` int(11) NOT NULL DEFAULT '0',
  `adresseIP` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_auteur_sujet` (`auteur`),
  KEY `fk_forum_sujet` (`forum`),
  CONSTRAINT `fk_auteur_sujet` FOREIGN KEY (`auteur`) REFERENCES `membre` (`id`),
  CONSTRAINT `fk_forum_sujet` FOREIGN KEY (`forum`) REFERENCES `forum_forum` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_sujet`
--

LOCK TABLES `forum_sujet` WRITE;
/*!40000 ALTER TABLE `forum_sujet` DISABLE KEYS */;
INSERT INTO `forum_sujet` VALUES (1,'Un sujet au pif','','Alors que dans la moule d\'argentine la teneur en mouléïte n\'est que de 6.7mg/L, dans une mole de moules du Bigoudène nous pouvons retrouver 647mg/L, ce qui signifie indubitablement la supériorité des moules fraîches sur les moules latines.',1,2,'2013-03-21 16:13:44',0,1,0,0,0,2,'192.168.1.2'),(2,'Alignement d\'un bloc horizontalement et verticalement','blabla CSS','dfsd f sdf sdfmjsd gp?diogj sd\r\nf$gl ds$fpgojk sdpgl dqf$gpk \r\nsdfôgj d\r\nspgjk sd$pflgjk zàçij p$)àa i =)\r\nkàpf\r\n$ sdlg\r\n sd=$k',1,1,'2013-04-17 10:22:06',1,0,0,0,0,0,'127.0.0.1'),(3,'Balise canvas et jQuery','Mic mac (do)','Rédigez votre message ici',2,1,'2013-04-17 10:40:42',0,0,0,0,0,0,'127.0.0.1'),(4,'test numéro 3','vérif de l\'action','test',2,1,'2013-04-17 10:42:22',0,0,1,0,0,0,'127.0.0.1'),(5,'Blablaz','sodfsdpfij sdfpi d','dfo sij sdfuohsd opusdh opfisdf',1,1,'2013-04-17 10:43:46',0,0,0,0,0,0,'127.0.0.1'),(6,'Ca devient casse-couilles là...','Cette histoire de redirection après submit','Ca va bien un moment...',2,1,'2013-04-17 10:45:57',1,1,1,0,0,0,'127.0.0.1'),(7,'sdfsd ','sdf sdf ','sdf sdf ',1,1,'2013-04-17 10:46:44',0,0,0,0,0,0,'127.0.0.1'),(8,'Dernier essai de création + redirection après succès...','ou pas','???',2,1,'2013-04-17 10:49:46',0,0,0,0,0,0,'127.0.0.1'),(9,'Boucle forEach sur un resultat SQL',NULL,'blabkjkfsdo jkâzopdjkqs^piojsiopfj sdiofjqp soidfj qsd\r\nfg$sdfgk fpgoj \r\nqsdfp?k \r\nsdf$pk spgiojsdfo jkd$gopd$sfg p\r\nopjazàeiopj sdjaioàzjqdà_çdjpoé\"jd) çéu \"çà&é)éuéi\"ç) éè_\"çà)',1,3,'2013-04-17 10:56:51',0,0,0,0,0,0,'127.0.0.1'),(10,'Création pour test growl',NULL,'Avec succès du message cette fois.',2,3,'2013-04-17 11:16:50',0,0,0,0,0,0,'127.0.0.1'),(11,'Boucle forEach sur un resultat SQL','cxcv','xcvxcv ',2,3,'2013-04-17 11:17:36',0,1,0,0,0,0,'127.0.0.1'),(12,'Avec un sous-titre','histoire de vérifier qu\'on l\'affiche bien derrière','sdfsdf sdf sdf ',2,1,'2013-04-17 11:18:32',0,0,0,0,0,0,'127.0.0.1'),(13,'tt',NULL,'t',2,2,'2013-04-19 19:21:17',0,0,0,0,0,2,'0:0:0:0:0:0:0:1%0'),(14,'WTF',NULL,'caca',1,2,'2013-04-19 19:24:21',0,0,0,0,0,1,'0:0:0:0:0:0:0:1%0'),(15,'Un sujet de plus pour tester la pagination...',NULL,'blabla',1,1,'2013-04-21 19:15:19',0,0,0,0,0,0,'0:0:0:0:0:0:0:1%0'),(16,'test',NULL,'test',1,1,'2013-04-21 19:15:30',0,0,0,0,0,0,'0:0:0:0:0:0:0:1%0'),(17,'Surement pas',NULL,'Ça me ferait mal aux ******.',1,1,'2013-04-21 19:15:52',0,0,0,0,0,0,'0:0:0:0:0:0:0:1%0'),(18,'Encore un',NULL,'on ne l\'arrête plus!',1,1,'2013-04-21 19:16:10',0,0,0,0,0,0,'0:0:0:0:0:0:0:1%0'),(19,'Vous en reprendrez bien un petit dernier pour la route ?',NULL,'Oui merci, juste un doigt.',1,1,'2013-04-21 19:16:37',0,0,0,0,0,0,'0:0:0:0:0:0:0:1%0');
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
  `id_objet` int(11) NOT NULL,
  `type_objet` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `valeur` tinyint(1) NOT NULL DEFAULT '0',
  UNIQUE KEY `id_membre` (`id_membre`,`id_objet`,`type_objet`),
  KEY `fk_membre_vote` (`id_membre`),
  CONSTRAINT `fk_membre_vote` FOREIGN KEY (`id_membre`) REFERENCES `membre` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_vote`
--

LOCK TABLES `forum_vote` WRITE;
/*!40000 ALTER TABLE `forum_vote` DISABLE KEYS */;
INSERT INTO `forum_vote` VALUES (1,1,'reponse',1),(1,1,'sujet',-1),(1,3,'reponse',-1),(1,4,'reponse',-1),(1,13,'sujet',-1),(1,14,'sujet',-1),(2,1,'reponse',-1),(2,1,'sujet',-1),(2,2,'reponse',1),(2,3,'reponse',-1),(2,4,'reponse',-1),(2,6,'reponse',-1),(2,13,'sujet',-1);
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
  `email` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `motDePasse` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `pseudo` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `prenom` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nom` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dateInscription` datetime NOT NULL,
  `dateDerniereConnexion` datetime DEFAULT NULL,
  `droits` int(2) DEFAULT '1',
  `avatar` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `signature` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `pseudo` (`pseudo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membre`
--

LOCK TABLES `membre` WRITE;
/*!40000 ALTER TABLE `membre` DISABLE KEYS */;
INSERT INTO `membre` VALUES (1,'coyote@test.com','baSDuV8d+My8W/M84MTBK42acjjKWeY9RrfboLDH/R7ot72cCi/KkA==','Med',NULL,NULL,'2013-03-21 16:12:48',NULL,1,NULL,'Compte de test pour voir la tronche des signatures.'),(2,'coy@ote.fr','baSDuV8d+My8W/M84MTBK42acjjKWeY9RrfboLDH/R7ot72cCi/KkA==','Coyote',NULL,NULL,'2013-03-21 16:12:48',NULL,4,NULL,'Créez votre application avec Java EE');
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

-- Dump completed on 2013-05-06 15:01:29
