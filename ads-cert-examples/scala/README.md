## Run
Usage:
```
sbt "run [private file] [public file]"
```
Example:
```
sbt "run ../private.pem ../public.pem"
```
Test mismatch:
```
sbt "run ../private.pem ../public_bad.pem"
```

## Dependency Scope
The library bouncycastle is used in this example, which is a signed jar and cannot be packaged into an uber-jar/shaded jar because it will remove the signature. Generally, this is marked as "provided" in your build.sbt and is provided at runtime.

build.sbt scope example:
```
libraryDependencies += "org.bouncycastle" % "bcpkix-jdk15on" % "1.59" % "provided"
```