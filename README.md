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
Launch the Localstack container before running integration tests
```
docker-compose up localstack
```

## 

```
aws s3 ls --endpoint-url http://localhost:4576
aws sqs list-queues
```
