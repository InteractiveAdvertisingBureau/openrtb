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