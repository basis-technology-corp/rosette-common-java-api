
### How to push the Maven site to gh-pages ###

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
