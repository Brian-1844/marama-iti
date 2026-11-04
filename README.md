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
- **Taille du texte** réglable et **lecture à voix haute**.
- Fonctionne **hors ligne** une fois installée.

## Installer sur un téléphone

**Version web (le plus simple)** : ouvrez le lien GitHub Pages dans Chrome, puis menu ⋮ → *Ajouter à l'écran d'accueil*.

**Version Android (.apk)** : onglet *Actions* du dépôt → dernière construction réussie → téléchargez *Marama-Iti-APK*, décompressez, puis ouvrez `app-debug.apk` sur le téléphone (autorisez l'installation depuis cette source).

## Modifier l'application

Tout se trouve dans `docs/index.html` :

- `const S` : les textes de l'interface (`ty` = reo tahiti, `fr` = français)
- `const DICT` : le dictionnaire des mots difficiles
- `const EX` : les exemples de courriers
- `const GUIDES` : les guides pas à pas
- `const CONTACTS` : les contacts (numéros d'exemple à remplacer)

Après une modification, changez `maramaiti-v1` en `maramaiti-v2` dans `docs/sw.js` pour que les téléphones reçoivent la nouvelle version.

## Traductions

Les textes en reo tahiti doivent être relus par un locuteur natif. Les corrections sont les bienvenues : ouvrez une *Issue* ou proposez une modification.

## Licence

MIT, libre d'utilisation et de modification.
