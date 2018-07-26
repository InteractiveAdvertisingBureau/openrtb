## Run
Usage:
```
mvn -q clean package && mvn -q exec:java -Dexec.args="[private file] [public file]"
```
Example:
```
mvn -q clean package && mvn -q exec:java -Dexec.args="../private.pem ../public.pem"
```
Test mismatch:
```
mvn -q clean package && mvn -q exec:java -Dexec.args="../private.pem ../public_bad.pem"
```

## Dependency Scope
The library bouncycastle is used in this example, which is a signed jar and cannot be packaged into an uber-jar/shaded jar because it will remove the signature. Generally, this is marked as "provided" in your pom.xml and is provided at runtime.

pom.xml scope example:
```
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcpkix-jdk15on</artifactId>
    <version>1.59</version>
    <scope>provided</scope>
</dependency>
```