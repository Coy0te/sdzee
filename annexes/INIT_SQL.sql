/* Commande de création d'une base vierge */
CREATE DATABASE bdd_sdzee DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
use bdd_sdzee;

/* Commande de création d'un utilisateur aux droits limités pour l'appli */
CREATE USER 'java'@'localhost' IDENTIFIED BY 'SdZ_eE';
GRANT ALL ON bdd_sdzee.* TO 'java'@'localhost' IDENTIFIED BY 'SdZ_eE';

/* Exemples de commandes de création d'index */
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
