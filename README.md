# Marama Iti

**To 'oe tauturu 'ohie · Votre assistant simplifié**

*Marama iti* : « la petite lumière », celle qui rend les choses claires.

Une application pour aider les personnes âgées à utiliser leur smartphone ou leur tablette, en **reo tahiti** et en **français**.

## Ce que fait l'application

- **Comprendre un texte difficile** : on colle un message (CPS, banque, SMS…) et les mots compliqués sont surlignés en jaune, avec une explication simple dans les deux langues.
- **Alerte arnaque** : un message qui demande de cliquer sur un lien et de payer déclenche un avertissement rouge.
- **Guides pas à pas** : envoyer une photo, repérer une arnaque, répondre à un appel.
- **Appeler un proche** avec de gros boutons, et numéros d'urgence de Polynésie (15, 17, 18).
- **Trois modes de langue** : Reo Tahiti, Français, ou les deux.
- **Taille du texte** réglable et **lecture à voix haute** : un bouton « Écouter » sur chaque texte, chaque mot expliqué, chaque alerte et l'heure de l'accueil.
- **Grands boutons** d'au moins 14 mm environ, suivant la recherche sur les utilisateurs âgés.
- Fonctionne **hors ligne** une fois installée.
- **Écran d'accueil du téléphone (launcher)** : sur Android, Marama Iti peut remplacer l'écran d'accueil habituel, avec l'heure en grand, la date en reo tahiti et en français, la batterie, et de grandes icônes pour les applications choisies.
- **Parler** : un grand bouton micro pour dire « Appelle Marie », « Ouvre WhatsApp », « Mets la radio », « Quelle heure est-il ? », « Cherche la météo » (reconnaissance vocale d'Android, en français).
- **Ma journée** : question du matin « Comment vous sentez-vous ? », pilulier virtuel avec quatre cases (Matin, Midi, Soir, Coucher) où l'on écrit chaque médicament et le nombre à prendre, avec une annonce vocale répétée toutes les 5 minutes jusqu'à « J'ai pris », agenda des rendez-vous (rappel une heure avant) et réveil réglé dans l'horloge du téléphone.
- **Accueil personnalisé** : prénom, message de la famille et message vocal enregistré par un proche, joué à la première ouverture de la journée.
- **Loisirs** : Internet simplifié (grande recherche et sites favoris), radios polynésiennes, musique (hīmene, musique tahitienne) et des jeux : Mémoire, Mots tahitiens, les jeux installés depuis le Play Store que la famille choisit, et des jeux en ligne ajoutés par leur adresse.
- **Réglages famille** protégés par un code à 4 chiffres : choisir les applications affichées, modifier les contacts, faire de Marama Iti l'écran d'accueil.

## Installer sur un téléphone

**Version web (le plus simple)** : ouvrez le lien GitHub Pages dans Chrome, puis menu ⋮ → *Ajouter à l'écran d'accueil*.

**Version Android (.apk)** : téléchargez directement la dernière version sur le téléphone :
https://github.com/Brian-1844/marama-iti/releases/latest/download/marama-iti.apk
Ouvrez le fichier et autorisez l'installation depuis cette source.

**En faire l'écran d'accueil** : ouvrez Marama Iti → *Réglages famille* (en bas de l'accueil) → créez le code → *Faire de Marama Iti l'écran d'accueil* → choisissez « Marama Iti ». Pour revenir à l'écran Android habituel : Réglages Android → Applications → Applications par défaut → Application d'accueil.

## Modifier l'application

Tout se trouve dans `docs/index.html` :

- `const S` : les textes de l'interface (`ty` = reo tahiti, `fr` = français)
- `const DICT` : le dictionnaire des mots difficiles
- `const EX` : les exemples de courriers
- `const GUIDES` : les guides pas à pas
- `const DEFAULT_CONTACTS` : les contacts d'exemple (la famille les remplace dans l'application)
- `native/` : le module Android (liste et ouverture des applications, écran d'accueil)
- `scripts/patch_android.py` : déclare Marama Iti comme écran d'accueil possible

Après une modification, augmentez le numéro (`maramaiti-v2` → `maramaiti-v3`) dans `docs/sw.js` pour que les téléphones reçoivent la nouvelle version.

## Santé et vie privée

Marama Iti rappelle les prises de médicaments programmées par la famille : elle ne remplace pas l'avis du médecin ou du pharmacien, et ne donne aucune dose.
Le moral, les prises et les rendez-vous restent **uniquement sur le téléphone**. La famille les consulte dans « Réglages famille ». Aucun serveur n'est utilisé.

## Traductions

Les textes en reo tahiti doivent être relus par un locuteur natif. Les corrections sont les bienvenues : ouvrez une *Issue* ou proposez une modification.

## Licence

MIT, libre d'utilisation et de modification.
