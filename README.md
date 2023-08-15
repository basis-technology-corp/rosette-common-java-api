[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.basistech/rosette-common-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.basistech/rosette-common-api)

## Rosette Common Java API

This project provides enums, exception, and small utilities used across the Rosette Platform in Java.

## Build & Release Process

#### Development Build

```
mvn clean install -DskipITs
```

#### Maven Central

```
mvn --batch-mode \
    --activate-profiles release \
    release:prepare \
    release:perform \
    -Darguments='-Dgpg.passphrase=MY_PASSPHRASE -DskipITs'
```

#### Internal Release

TBD


