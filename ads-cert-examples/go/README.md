## Run
```
go run main.go [private file] [public file]
```
Example:
```
go run main.go ../private.pem ../public.pem
```
Test mismatch:
```
go run main.go ../private.pem ../public_bad.pem
```