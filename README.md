# Rosette Common Java #

This library contains Java classes common to all of the products. As
such, it ends up in the classpath of everything. Thus, we try to
ensure that new versions of it are as compatible as possible with
older versions, so that it is easier to combine multiple products in
one classpath.

See RELEASE-NOTES.md for information about particular changes.

# Structure #

There are two modules in here: `lib` and `t5builder`. `lib` is the
primary library of common classes, and the remarks about compatibility
above primarily apply in here. `t5builder` contains the Java
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
