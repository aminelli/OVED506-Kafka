# Docker kafka test

## Step 1 - Creazione rete

```shell
docker network create net-kafka
```

## Step 2 - Creazione kafka standalone

```shell
docker run -d --hostname kafka01 --name kafka01 -p 9092:9092 --network net-kafka  apache/kafka:3.9.0
```
