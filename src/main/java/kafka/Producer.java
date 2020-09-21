package kafka;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.kafka.client.producer.RecordMetadata;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.kafka.client.producer.KafkaProducer;
import io.vertx.reactivex.kafka.client.producer.KafkaProducerRecord;


import java.util.HashMap;
import java.util.Map;

public class Producer extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    KafkaProducer<String, String> producer;
    TestCallback callback;


    /**
     * Method to create producer to sent events
     */
    public Producer(Vertx vertx) {

        producer = KafkaProducer.create(vertx, initProperties());
        callback = new TestCallback();

    }

    public void sendBarring(String barring) {

        KafkaProducerRecord data = KafkaProducerRecord.create("test", barring);

        producer.send(data, callback);

        //producer.close();
    }

    /**
     * Method to inicializate Properties to Kafka producer
     *
     * @return config properties with values
     */
    public Map<String, String> initProperties() {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:29092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");

        return config;
    }

    /**
     * Method to get callback when events sends, print result in log
     */
    private static class TestCallback implements Handler<AsyncResult<RecordMetadata>> {

        @Override
        public void handle(AsyncResult<RecordMetadata> event) {

            if (event == null) {
                LOGGER.error("The event could not be sent:" + event.result());
            } else {
                String message = String.format("The event was sent to topic :%s partition :%s  offset:%s",
                        event.result().getTopic(), event.result().getPartition(), event.result().getOffset());
                LOGGER.info(message);
            }

        }
    }
}
