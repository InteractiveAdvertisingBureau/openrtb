## Generate Keys
```
openssl ecparam -out private.pem -name prime256v1 -genkey
openssl ec -in private.pem -pubout -out public.pem
```
If you want to test key mismatches you can generate a second pair of keys.
```
openssl ecparam -out private_bad.pem -name prime256v1 -genkey
openssl ec -in private_bad.pem -pubout -out public_bad.pem
```