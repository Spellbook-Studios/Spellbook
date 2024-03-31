![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![GitHub](https://img.shields.io/github/license/Spellbook-Studios/Spellbook?style=for-the-badge)
![Maven metadata URL](https://img.shields.io/maven-metadata/v?label=SNAPSHOT&metadataUrl=https://maven.sebsa.dk/snapshots/dk/sebsa/spellbook/maven-metadata.xml&style=for-the-badge)
<a href="https://spellbook-studios.gitbook.io/spellbook/"><img alt="gitbook" height="30" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/documentation/gitbook_vector.svg"></a>
![GitHub Repo stars](https://img.shields.io/github/stars/Spellbook-Studios/Spellbook?style=social) [![Quality Check](https://github.com/Spellbook-Studios/Spellbook/actions/workflows/qodana.yml/badge.svg)](https://github.com/Spellbook-Studios/Spellbook/actions/workflows/qodana.yml)

# Spellbook

**TODO: Description**

## Features

**TODO: Features**

## Using Spellbook

**Game Engine** You can use our own game editor [Arcana](https://github.com/Spellbook-Studios/Arcana), which is based
made with Spellbook and uses Spellbook both in engine and in runtime.

**Library** You can also use Spellbook directly as a java library
<br/>For information about working with spellbook, spellbook-gradle and creating spellbook applications
look at [docs/developing-with-spellbook.md](https://github.com/Spellbook-Studios/Spellbook/blob/main/docs/developing-with-spellbook.md)

**Developing Spellbook**
For information on how Spellbook works and how to work with this repository please look at [docs/developing-spellbook.md](https://github.com/Spellbook-Studios/Spellbook/blob/main/docs/developing-spellbook.md)

Spellbook has a fully fledged JavaDoc that has description for all methods, functions, classes and enums
[READ THE DOCS](https://maven.sebsa.dk/javadoc/snapshots/dk/sebsa/spellbook/1.0.0-SNAPSHOT)

*With love, Sebastian :heart:*

### In this repository
* demos/ - Demo games and a template to make more demos. Feel free to make one yourself and upload it to this folder.
* docs/ - Documentation about using Spellbook. For information about specific functions, methods and classes, please read the [JavaDoc](https://maven.sebsa.dk/javadoc/snapshots/dk/sebsa/spellbook/1.0.0-SNAPSHOT)
* local/ - A folder for your own testing and development projects. A template is provided inside and new projects are automatically added to gradle. Your own projects are not synced to this repository
* sandbox/ - A shared sandbox application used to develop Spellbook
* spellbook/ - The Spellbook java library
* spellbook-gradle/ - Spellbook Gradle Plugin. The plugin automatically adds the shared depedencies and natives, and also ensures that the correct java version is used.