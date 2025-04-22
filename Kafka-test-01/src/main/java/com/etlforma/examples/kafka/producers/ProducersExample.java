package com.etlforma.examples.kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

public class ProducersExample {


    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        var producer01 = new ProducersExample();

        // producer01.sendMessagesFireAndForget("T_FireAndForget", 1);
        producer01.sendMessagesAck1Sync("T_Ack1_sync", 100000);


    }



    // FIRE & FORGET | ACK = 0
    public void sendMessagesFireAndForget(String topicName, long totalMessage) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Properties props = new Properties();

        // Connessione al cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

        // Client ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "ProducerFAF");

        // Compression Type
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        // TIPO ACK = 0
        props.put(ProducerConfig.ACKS_CONFIG, "0");

        // Namespaces classi per serializzazione
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,   "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        ProducerRecord<String, String> record = null;

        String startTime = formatter.format(new Date());

        for (int count = 0; count < totalMessage; count++) {

            record = new ProducerRecord<String, String>(topicName, "Messaggio nr " + count);
            String headKey = "K_" + startTime + "_" + count;

            try {
                record.headers().add("MSG_KEY", headKey.getBytes());
                producer.send(record);
                System.out.println("Sent message -> " + headKey);
            } catch (Exception ex) {
                System.out.println("Error Msg -> " + headKey);

            }

        }

        String endTime = formatter.format(new Date());

        System.out.println("Start: -> " + startTime);
        System.out.println("End:   -> " + endTime);

        producer.flush();
        producer.close();
        System.out.println("Done \r\n\r\n");

    }


    // ACK = 1 con sync
    public void sendMessagesAck1Sync(String topicName, long totalMessage) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Properties props = new Properties();

        // Connessione al cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

        // Client ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "ProducerFAF");

        // Compression Type
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        // TIPO ACK = 0
        props.put(ProducerConfig.ACKS_CONFIG, "1");

        // Namespaces classi per serializzazione
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,   "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        ProducerRecord<String, String> record = null;

        String startTime = formatter.format(new Date());

        for (int count = 0; count < totalMessage; count++) {

            record = new ProducerRecord<String, String>(topicName, "Messaggio nr " + count);
            String headKey = "K_" + startTime + "_" + count;

            try {
                record.headers().add("MSG_KEY", headKey.getBytes());
                Future<RecordMetadata> future = producer.send(record);

                RecordMetadata metadata = future.get();


                System.out.println("Sent message -> " + headKey);

                System.out.println("Partition -> " + metadata.partition());
                System.out.println("Offset -> " + metadata.offset());
                System.out.println("======================================");


            } catch (Exception ex) {
                System.out.println("Error Msg -> " + headKey);

            }

        }

        String endTime = formatter.format(new Date());

        System.out.println("Start: -> " + startTime);
        System.out.println("End:   -> " + endTime);

        producer.flush();
        producer.close();
        System.out.println("Done \r\n\r\n");




    }




}
