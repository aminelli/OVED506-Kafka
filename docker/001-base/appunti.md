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

## Step 3 - Creazione Topic e creazione di un producer

```shell
docker exec -it kafka01 /bin/bash
cd opt/kafka/bin
./kafka-topics.sh --create --topic test-corso --bootstrap-server localhost:9092
./kafka-topics.sh --describe --topic test-corso --bootstrap-server localhost:9092
./kafka-console-producer.sh --topic test-corso --bootstrap-server localhost:9092
```

## Step 4 - Creazione di un consumer

```shell
docker exec -it kafka01 /bin/bash
cd opt/kafka/bin
./kafka-console-consumer.sh --topic test-corso --from-beginning --bootstrap-server localhost:9092
```


## NOTA: Come entrare con processo root e isntallare tools (es nano, htop)

```shell
docker exec -u root -it kafka01 /bin/bash
apk update
apk search htop
apk add htop
apk add nano
```

## Esercizio - Monitoring di un file txt tramite un kafka connect

### Step 1 - Configurazione e avvio di un kafka connect per monitorare il file txt

#### Configurazione Producer

```shell
docker exec -it kafka01 /bin/bash
cd /opt/kafka
echo "plugin.path=libs/connect-file-3.9.0.jar" >> config/connect-standalone.properties
echo -e "msg1\nmsg2\nmsg3\nmsg3\nmsg4\nmsg5\nmsg6\nmsg7\nmsg8\nmsg9\nmsg10\nmsg11" > test.txt
bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties
```

#### Avvio Consumer

```shell
docker exec -it kafka01 /bin/bash
cd /opt/kafka/
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic connect-test --from-beginning 
```

#### Avvio di un altro terminale per aggiungere righe al file txt

```shell
docker exec -it kafka01 /bin/bash
cd /opt/kafka/
echo "ciao" >> test.txt
```
