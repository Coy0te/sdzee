sdzee
=====

Projet communautaire réalisé à l'aide des technos Java EE / JPA / JSF.  
Développé et testé avec :
- Glassfish 3.1.2.2 :
   - Java EE 6
   - JPA 2 (EclipseLink)
   - JSF 2.1 (Mojarra 2.1.21) 
- OmniFaces 1.4.1 
- PrettyFaces 3.3.3
- MySQL 5.5.28

Fonctionnalités implementées
----------------------------
### La gestion des forums
#### Un membre peut :
* Créer des topics dans les forums
* Écrire en syntaxe markdown évoluée (TODO: barre d'édition, smileys et optimisation ressources parseur)
* Éditer ses propres topic (en mode ajaxisé)
* Marquer les réponses utiles dans ses topics
* Marquer ses topics comme résolus
* Poster dans les topics des autres
* Être averti si le topic auquel il répond a changé (nouveaux posts non lus)
* Voter sur les posts des autres (en mode ajaxisé)
* Signaler des posts aux modérateurs (TODO : permettre la saisie d'un message d'alerte)
* Être notifié des réponses aux topics dans lesquels il a posté
* Marquer un topic en favori
* Être notifié des réponses aux topics qu'il a marqués en favori
* Choisir de ne plus être notifié de certains topics
* Faire disparaître les notifs en consultant les topics concernés

#### Un staff peut :
* Faire tout ce que peut faire un membre simple
* Masquer un post (TODO: backend ok, frontend à terminer)
* Supprimer un post (TODO: backend à tester, frontend à réaliser)
* Supprimer un topic (TODO: backend à tester, frontend à réaliser)
* Résoudre une alerte sur un post (TODO)
* Marquer n'importe quel topic comme résolu
* Mettre n'importe quel topic en post-it
* Verrouiller n'importe quel topic
* Déplacer n'importe quel topic dans un autre forum


### La gestion des membres
#### Un membre peut :
* Modifier son profil (bio, site, avatar, signature, etc.)
* Modifier ses préférences
    * Affichage public de son adresse mail
    * Affichage de la signature des membres dans les forums
* Modifier son mot de passe
* Modifier son pseudo
* Modifier son adresse mail

#### Un staff peut :
* Faire tout ce que peut faire un membre simple
* Bannir des membres (temporairement ou à vie) (TODO)
* Mettre des membres en lecture seule (temporairement ou à vie) (TODO)


### La gestion des Messages Privés
#### Un membre peut :
* Créer des MP
* Ajouter des membres aux MP dont il est l'auteur
* Répondre aux MP dans lesquels il participe
* Être averti si le MP auquel il répond a changé (nouveaux posts non lus)
* Sortir d'un MP sans supprimer la conversation (TODO)
* Éditer son post si la dernière réponse postée dans le MP est la sienne
* Supprimer un MP de sa boîte de réception (TODO)
* Être notifié de ses nouveaux MP, et des nouvelles réponses aux MP auxquels il participe
* Faire disparaître les notifs en consultant les MP concernés

#### Un staff peut :
* Faire tout ce que peut faire un membre simple
* Éditer n'importe quel post dans un MP auquel il participe
* Ajouter des membres dans un MP auquel il participe


Fonctionnalités à venir
------------------------
- ban membre / ban IP / LS
- récupération du compte via email (mot de passe et/ou login oubliés)
- les tutos en lecture seule
- la rédaction et la validation des tutos
- la home
- mise en place d'un logger
- rédaction des messages de validation
- internationalisation
- les articles


Comment démarrer une instance de sdzee ?
----------------------------------------
### Pré-requis
- Installez le dernier MySQL 5.x sur votre poste
- Récupérez et décompressez le zip de Glassfish 3.1.2.2 ([direct download](http://download.java.net/glassfish/3.1.2.2/release/glassfish-3.1.2.2-web.zip))

### Config Glassfish
1. Allez dans le dossier **glassfish3/bin**, et lancez l'exécutable **asadmin**
2. Démarrez le serveur en entrant la commande `start-domain` 
3. Entrez ensuite la commande `add-resources <chemin-vers-le-fichier-bonecp_datasource.xml-dans-le-repertoire-annexes>`
4. Fermez l'utilitaire **asadmin**
5. Remplacez le Jar de Mojarra (javax.faces.jar) dans le répertoire **glassfish3/glassfish/modules/** par celui foruni dans **annexes**
5. Copiez les fichiers Jar de boneCP, guava, slf4j, mysql-connector, parboiled, pegdown, et asm-all (tous présents dans le dossier **annexes** du projet) dans le répertoire **glassfish3/glassfish/domains/domain1/lib/ext/**

### Config MySQL
1. Ouvrez un terminal MySQL
2. Créez la base : `CREATE DATABASE bdd_sdzee DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;`
3. Créez un utilisateur : `CREATE USER 'java'@'localhost' IDENTIFIED BY 'SdZ_eE';`
4. Donnez-lui les droits : `GRANT ALL ON bdd_sdzee.* TO 'java'@'localhost' IDENTIFIED BY 'SdZ_eE';`
5. Quittez le terminal MySQL, et ouvrez un terminal système
6. Placez-vous dans le dossier **annexes** du projet
7. Intégrez les données de test du start-dump : `mysql -h localhost -u java -pSdZ_eE bdd_sdzee < bdd_sdzee.sql`

### Déploiement
Copiez l'intégralité du dossier mère **sdzee** dans le répertoire **glassfish3/glassfish/domains/domain1/applications/**

Lancez enfin Glassfish, et accédez au site depuis l'URL `http://localhost:8080/sdzee/`.
