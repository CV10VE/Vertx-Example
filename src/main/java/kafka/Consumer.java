package kafka;

import io.reactivex.Flowable;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.kafka.client.consumer.KafkaConsumer;
import io.vertx.reactivex.kafka.client.consumer.KafkaConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Consumer extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    //private KafkaReadStream<String, String> consumer;

    @Override
    public void start(Future<Void> fut) {

        Map config = initProperties();

        Flowable<KafkaConsumerRecord> stream = KafkaConsumer.<String, String>create(vertx, config)
                .subscribe("test")
                .toFlowable();

        stream
                .subscribe(data -> {
                    LOGGER.info("Consuming event: {}", data.value());
                    System.out.println(data.value());
                });

        fut.complete();
    }

    /**
     * Method to inicializate Properties to Kafka consumer
     *
     * @return config properties with values
     */
    public Map<String, String> initProperties() {
        Map config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "my_group");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return config;
    }
}
