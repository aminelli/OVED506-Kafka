# Docker kafka test

## Step 1 - Creazione rete

```shell
docker network create net-kafka
```

## Step 2 - Creazione kafka standalone

```shell
docker run -d --hostname kafka01 --name kafka01 -p 9092:9092 --network net-kafka  apache/kafka:3.9.0
docker logs -f kafka01
```

## Step 3 - Creazione Topic

```shell
docker exec -it kafka01 /bin/bash
cd opt/kafka/bin
./kafka-topics.sh --create --topic test-corso --bootstrap-server localhost:9092
./kafka-topics.sh --describe --topic test-corso --bootstrap-server localhost:9092
```
