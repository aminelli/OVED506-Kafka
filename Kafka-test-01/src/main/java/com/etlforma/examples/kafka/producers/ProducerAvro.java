package com.etlforma.examples.kafka.producers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.text.SimpleDateFormat;
import java.util.Properties;


import io.confluent.kafka.serializers.KafkaAvroSerializer;
import com.etlforma.examples.kafka.model.CustomerAvro;


public class ProducerAvro {

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        var producer = new ProducerAvro();

        producer.sendSyncCustomer("T_AVRO_EXAMPLE", 1000);

    }


    public void sendSyncCustomer(String topicName, long totalMessage) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Properties props = new Properties();

        // Definiamo connessione al cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

        // Schema Registry
        props.put("schema.registry.url", "http://localhost:8081");


        // Client ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "PCustomer");

        // Compression Type
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        // TIPO ACK
        props.put(ProducerConfig.ACKS_CONFIG, "1");

        // Namespaces delle CLASSI da utilizzare per la serializzazione della KEY e del VALORE
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);


        KafkaProducer<String, CustomerAvro> producer = new KafkaProducer<>(props);

        String startTime = formatter.format(new Date());

        for (int count = 0; count < totalMessage; count++) {

            CustomerAvro obj = new CustomerAvro();
            obj.setId(count + 1);
            obj.setLastName("BIANCHI");
            obj.setFirstName("Mario " + (count + 1));

            // USO DI CUSTOMER AVRO
            ProducerRecord<String, CustomerAvro> record = new ProducerRecord<String, CustomerAvro>(topicName, obj);

            // USO DI GENERIC DATA SU MODEL NON NATIVO AVRO
            //ProducerRecord<String, GenericRecord> record = new ProducerRecord<String, GenericRecord>("TP_CUSTOMER_AVRO", obj);
            //Customer customer = new Customer();
            //Schema sc =Schema.parse("CustomerAvro.avsc");
            //GenericRecord rec = customer.toAvro(sc);


            try {

                Future<RecordMetadata> future = producer.send(record);

                RecordMetadata metadata = future.get();
                // printMetadata(metadata);


                System.out.println("Sended Msg -> " + metadata.offset() + " " + obj.getLastName() + " " + obj.getFirstName());
            } catch (Exception e) {
                System.out.println("Error Msg -> " + e.getMessage());
            }
        }
        System.out.println("START -> " + startTime + " == END -> " + formatter.format(new Date()));

        System.out.println("Sended Closed");
        producer.flush();
        producer.close();
        System.out.println("Complited");

    }



}
