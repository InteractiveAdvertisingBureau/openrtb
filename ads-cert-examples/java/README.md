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