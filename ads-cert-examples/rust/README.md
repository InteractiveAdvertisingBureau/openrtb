## Run
```
cargo build && ./target/debug/adcert-rust [private file] [public file]
```
Example:
```
cargo build && ./target/debug/adcert-rust ../private.pem ../public.pem
```
Test mismatch:
```
cargo build && ./target/debug/adcert-rust ../private.pem ../public_bad.pem
```