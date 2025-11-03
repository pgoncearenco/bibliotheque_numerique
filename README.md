# Bibliothèque Numérique

Ce projet est une application Java permettant de gérer une bibliothèque numérique.  
Elle offre la gestion des médias (Livres, Magazines, CD, DVD), des membres et des emprunts.

## Fonctionnalités

- Ajouter / lister / rechercher des médias
- Inscription et gestion des membres
- Emprunt et retour des médias
- Calcul des amendes en cas de retard
- Sauvegarde automatique des données

## Structure du projet

bibliotheque_numerique/
├── models/ # Classes métier (Media, Livre, CD, etc.)
├── exceptions/ # Exceptions personnalisées
├── services/ # Logique métier (Bibliotheque, Emprunts, etc.)
├── ui/ # Interface utilisateur (menu console)
├── utils/ # Outils : dates & validation
└── Main.java # Point d'entrée du programme

## Compilation et Exécution

Depuis la racine du projet :

```bash
javac models/*.java exceptions/*.java services/*.java ui/*.java utils/*.java Main.java
java Main

```
## Technologies utilisées
Java 17+ (compatible Java 11)

Collections Java (HashMap, ArrayList)

Sérialisation (ObjectOutputStream / ObjectInputStream)

## Auteur 
Petru GONCEARENCO

Projet réalisé dans le cadre d'apprentissage du langage Java.
