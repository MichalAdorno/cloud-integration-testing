## Requirements

* Java SE 11
* Docker
* AWS CLI

## Localstack

Calling AWS:

```
aws s3 ls
```

Calling Localstack (the AWS emulator):

```
aws s3 ls --endpoint-url http://localhost:4572
```

## Running integration tests

```
./gradlew clean build
```

## Launching containers

Launch the Localstack containers

```
docker-compose up localstack
```
