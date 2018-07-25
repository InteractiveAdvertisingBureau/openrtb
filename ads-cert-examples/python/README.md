## Run
Install dependencies:
```
pip install -r requirements.txt
```
Usage:
```
python main.py [private file] [public file]
```
Example:
```
go run main.go ../private.pem ../public.pem
```
Test mismatch:
```
go run main.go ../private.pem ../public_bad.pem
```