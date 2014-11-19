# Rosette Common Java #

This library contains Java classes common to all of the products. As
such, it ends up in the classpath of everything. Thus, we try to
ensure that new versions of it are as compatible as possible with
older versions, so that it is easier to combine multiple products in
one classpath.

See RELEASE-NOTES.md for information about particular changes.

```
$ MAVEN_OPTS=-Xmx2g mvn clean install
```

# Structure #

There are three modules in here: `common-api`, `common-lib` and
`common-t5builder`. `common-api` contains enums and exceptions that are part
 of the public API of just about everything that we build at
 Basis. The compatility requirements are strongest here.
 
`common-lib` is a collection of common classes, and the remarks about
compatibility also apply in here, but we plan to use classpath
isolation rather than guarantee perfect backwards compatibility to
allow mixing and matching of products. `common-t5builder` contains the Java
implementation of the code to build Take5 files. Each directory has
its own README.md with more information.

# How to push the Maven site to gh-pages #

```
  mvn site site:stage
  mvn scm-publish:publish-scm
```

Typically, this would be after a release:

```
  mvn release:perform
  cd target/checkout 
  mvn site site:stage
  mvn scm-publish:publish-scm
```
