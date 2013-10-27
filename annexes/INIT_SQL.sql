CREATE USER 'java'@'localhost' IDENTIFIED BY 'SdZ_eE';
GRANT ALL ON bdd_sdzee.* TO 'java'@'localhost' IDENTIFIED BY 'SdZ_eE';

CREATE TABLE  bdd_sdzee.licence (
  id INT( 2 ) NOT NULL AUTO_INCREMENT , 
  titre VARCHAR( 200 ) NOT NULL ,   
  lien VARCHAR( 200 ) NULL ,   
  description TEXT NULL ,
  PRIMARY KEY ( id )
)
ENGINE = INNODB;

CREATE TABLE  bdd_sdzee.tuto_btmtchap (
  id INT( 11 ) NOT NULL AUTO_INCREMENT ,  
  partie INT( 11 ) NULL ,  
  titre VARCHAR( 200 ) NOT NULL , 
  licence INT( 2 ) NULL ,  
  difficulte INT( 2 ) NULL ,  
  fini TINYINT( 1 ) NULL ,  
  introduction TEXT NULL ,
  conclusion TEXT NULL ,
  position INT(2) NOT NULL DEFAULT 0 ,
  dateCreation DATETIME NOT NULL ,  
  dateDerniereModification DATETIME NOT NULL ,  
  adresseIP VARCHAR( 50 ) NOT NULL ,
  PRIMARY KEY ( id ) ,
  CONSTRAINT fk_licence_btmtchap FOREIGN KEY (licence) REFERENCES licence(id)
)
ENGINE = INNODB;

CREATE TABLE  bdd_sdzee.tuto_partie (
  id INT( 11 ) NOT NULL AUTO_INCREMENT ,  
  bigtuto INT( 11 ) NOT NULL ,  
  titre VARCHAR( 200 ) NOT NULL , 
  difficulte INT( 2 ) NULL ,  
  introduction TEXT NULL ,
  conclusion TEXT NULL ,
  position INT(2) NOT NULL DEFAULT 0 ,
  dateCreation DATETIME NOT NULL ,  
  dateDerniereModification DATETIME NOT NULL ,  
  adresseIP VARCHAR( 50 ) NOT NULL ,
  PRIMARY KEY ( id ) ,
  CONSTRAINT fk_partie_bigtuto FOREIGN KEY (bigtuto) REFERENCES tuto_btmtchap(id) 
)
ENGINE = INNODB;

ALTER TABLE bdd_sdzee.tuto_btmtchap
ADD CONSTRAINT fk_partie_chapitre FOREIGN KEY (partie) REFERENCES tuto_partie(id);
  
CREATE TABLE  bdd_sdzee.tuto_souspartie (
  id INT( 11 ) NOT NULL AUTO_INCREMENT ,  
  mtchap INT( 11 ) NOT NULL ,  
  titre VARCHAR( 200 ) NOT NULL , 
  texte LONGTEXT NULL ,
  position INT(2) NOT NULL DEFAULT 0 ,
  dateCreation DATETIME NOT NULL ,  
  dateDerniereModification DATETIME NOT NULL ,  
  adresseIP VARCHAR( 50 ) NOT NULL ,
  PRIMARY KEY ( id ) ,
  CONSTRAINT fk_souspartie_mtchap FOREIGN KEY (mtchap) REFERENCES tuto_btmtchap(id) 
)
ENGINE = INNODB;

CREATE TABLE  bdd_sdzee.tuto_question (
  id INT( 11 ) NOT NULL AUTO_INCREMENT ,  
  mtchap INT( 11 ) NOT NULL ,  
  enonce TEXT NOT NULL , 
  explication TEXT NOT NULL ,
  position INT(2) NOT NULL DEFAULT 0 ,
  dateCreation DATETIME NOT NULL ,  
  dateDerniereModification DATETIME NOT NULL ,  
  adresseIP VARCHAR( 50 ) NOT NULL ,
  PRIMARY KEY ( id ) ,
  CONSTRAINT fk_question_mtchap FOREIGN KEY (mtchap) REFERENCES tuto_btmtchap(id) 
)
ENGINE = INNODB;

CREATE TABLE  bdd_sdzee.tuto_reponse (
  id INT( 11 ) NOT NULL AUTO_INCREMENT ,  
  question INT( 11 ) NOT NULL ,  
  texte TEXT NOT NULL , 
  correct TINYINT(1) NOT NULL ,
  dateCreation DATETIME NOT NULL ,  
  dateDerniereModification DATETIME NOT NULL ,  
  adresseIP VARCHAR( 50 ) NOT NULL ,
  PRIMARY KEY ( id ) ,
  CONSTRAINT fk_reponse_question FOREIGN KEY (question) REFERENCES tuto_question(id) 
)
ENGINE = INNODB;

CREATE TABLE  bdd_sdzee.tuto_join_auteurs_btmtchap (
  btmtchap INT( 11 ) NOT NULL , 
  auteur INT( 11 ) NOT NULL ,   
  CONSTRAINT fk_btmtchap_btmtchap FOREIGN KEY (btmtchap) REFERENCES tuto_btmtchap(id) ,
  CONSTRAINT fk_membre_auteur FOREIGN KEY (auteur) REFERENCES membre(id) ,
  UNIQUE KEY btmtchap (btmtchap , auteur)
)
ENGINE = INNODB;


ALTER TABLE bdd_sdzee.tuto_btmtchap ADD INDEX btmtchap_position_index (position);
ALTER TABLE bdd_sdzee.tuto_partie ADD INDEX partie_position_index (position);
ALTER TABLE bdd_sdzee.tuto_souspartie ADD INDEX souspartie_position_index (position);
ALTER TABLE bdd_sdzee.tuto_question ADD INDEX question_position_index (position);

/* Nettoyage de la base pour remise à plat après essai :
 * 
drop table tuto_souspartie,tuto_reponse,tuto_question,tuto_join_auteurs_btmtchap;
ALTER TABLE tuto_partie DROP FOREIGN KEY fk_partie_bigtuto;
ALTER TABLE tuto_btmtchap DROP FOREIGN KEY fk_licence_btmtchap;
drop table tuto_btmtchap, tuto_partie;
*/

/* Exemple de MAJ d'un index de position quand on déplace ou insère un chapitre ou une partie ou une sous-partie dans un cours :
 * 
UPDATE table SET position = position + 1 WHERE position >= 3;
INSERT INTO table (..., position) VALUES (..., 3);
 */
