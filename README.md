sdzee
=====

Projet communautaire réalisé à l'aide des technos Java EE / JPA / JSF. 
Développé et testé avec Glassfish et MySQL.

Fonctionnalités implementées
----------------------------
### La gestion des forums
#### Un membre peut :
* Créer des topics dans les forums
* Éditer ses propres topic (en mode ajaxisé)
* Marquer les réponses utiles dans ses topics
* Marquer ses topics comme résolus
* Poster dans les topics des autres (sans smileys)
* Voter sur les posts des autres
* Signaler des posts aux modérateurs
* Être notifié des réponses aux topics dans lesquels il a posté
* Marquer un topic en favori
* Être notifié des réponses aux topics qu'il a marqués en favori
* Choisir de ne plus être notifié de certains topics

#### Un staff peut :
* Faire tout ce que peut faire un membre simple
* Masquer ou supprimer un post
* Résoudre une alerte sur un post
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
* Bannir des membres (temporairement ou à vie)
* Mettre des membres en lecture seule (temporairement ou à vie)


### La gestion des Messages Privés
#### Un membre peut :
* Créer des MP
* Ajouter des membres aux MP dont il est l'auteur
* Répondre aux MP dans lesquels il participe
* Sortir d'un MP sans supprimer la conversation
* Éditer son message si la dernière réponse postée dans le MP est la sienne
* Supprimer un MP de sa boîte de réception

#### Un staff peut :
* Faire tout ce que peut faire un membre simple


**TODO** :
- ban membre / ban IP / LS
- récupération du compte via email (mot de passe et/ou login oubliés)
- les tutos en lecture seule
- la rédaction et la validation des tutos
- la home
- mise en place d'un logger
- rédaction des messages de validation
- internationalisation
